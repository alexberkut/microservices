package com.epam.resourceservice.service;

import com.epam.resourceservice.client.SongServiceClient;
import com.epam.resourceservice.dto.SongDto;
import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.repository.ResourceRepository;
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

    @Transactional
    public Long saveResource(byte[] data) {
        var resource = new Resource();
        resource.setData(data);
        var savedResource = resourceRepository.save(resource);
        sendMetadataToSongService(savedResource.getId(), data);
        return savedResource.getId();
    }

    public Resource getResource(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Long> deleteResources(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();

        List<Resource> resourcesToDelete = resourceRepository.findAllById(idList);
        List<Long> deletedIds = resourcesToDelete.stream()
                .map(Resource::getId)
                .toList();

        if (deletedIds.isEmpty()) {
            log.info("No resources found for the given IDs. Nothing to delete.");
            return List.of();
        }

        resourceRepository.deleteAll(resourcesToDelete);

        String deletedIdsString = deletedIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        songServiceClient.deleteSongMetadata(deletedIdsString);

        log.info("Successfully deleted resources with IDs: {}", deletedIds);
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

            var songDto = new SongDto(
                    id,
                    metadata.get("dc:title"),
                    metadata.get("xmpDM:artist"),
                    metadata.get("xmpDM:album"),
                    duration,
                    metadata.get("xmpDM:releaseDate")
            );

            songServiceClient.createSongMetadata(songDto);
        } catch (Exception e) {
            log.error("Error processing and sending metadata: {}. sendMetadataToSongService: id={}", e.getMessage(), id, e);
        }
    }
}
