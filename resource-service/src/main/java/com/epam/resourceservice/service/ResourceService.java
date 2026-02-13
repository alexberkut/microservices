package com.epam.resourceservice.service;

import com.epam.resourceservice.client.SongServiceClient;
import com.epam.resourceservice.dto.SongDto;
import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.exception.ResourceNotFoundException;
import com.epam.resourceservice.repository.ResourceRepository;
import com.epam.resourceservice.validation.ResourceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final SongServiceClient songServiceClient;
    private final ResourceValidator resourceValidator;

    @Transactional
    public Long saveResource(byte[] data) {
        var resource = new Resource();
        resource.setData(data);
        var savedResource = resourceRepository.save(resource);
        sendMetadataToSongService(savedResource.getId(), data);
        return savedResource.getId();
    }

    public Resource getResource(String id) {
        resourceValidator.validateId(id);
        var resourceId = Long.parseLong(id);
        return resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with ID=%s not found", id)));
    }

    @Transactional
    public List<Long> deleteResources(String ids) {
        resourceValidator.validateIds(ids);
        var idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        var resourcesToDelete = resourceRepository.findAllById(idList);
        var deletedIds = resourcesToDelete.stream().map(Resource::getId).toList();

        if (!deletedIds.isEmpty()) {
            resourceRepository.deleteAll(resourcesToDelete);
            var deletedIdsString = deletedIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            songServiceClient.deleteSongMetadata(deletedIdsString);
            log.info("Successfully deleted resources with IDs: {}", deletedIds);
        }

        return deletedIds;
    }

    private void sendMetadataToSongService(Long id, byte[] data) {
        try (var input = new ByteArrayInputStream(data)) {
            var handler = new BodyContentHandler();
            var metadata = new Metadata();
            var parseCtx = new ParseContext();
            var parser = new Mp3Parser();
            parser.parse(input, handler, metadata, parseCtx);

            var durationInSeconds = metadata.get("xmpDM:duration");
            var duration = "";
            if (durationInSeconds != null) {
                var seconds = Double.parseDouble(durationInSeconds);
                var minutes = (long) (seconds / 60);
                var remainingSeconds = (long) (seconds % 60);
                duration = String.format("%02d:%02d", minutes, remainingSeconds);
            }

            var year = "";
            var releaseDate = metadata.get("xmpDM:releaseDate");
            if (releaseDate != null && releaseDate.length() >= 4) {
                year = releaseDate.substring(0, 4);
            }

            var songDto = new SongDto(id, metadata.get("dc:title"), metadata.get("xmpDM:artist"), metadata.get("xmpDM:album"), duration, year);
            songServiceClient.createSongMetadata(songDto);
        } catch (Exception e) {
            log.error("Error processing and sending metadata: {}. sendMetadataToSongService: id={}", e.getMessage(), id, e);
        }
    }
}
