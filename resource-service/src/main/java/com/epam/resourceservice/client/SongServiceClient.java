package com.epam.resourceservice.client;

import com.epam.resourceservice.dto.SongDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
public class SongServiceClient {

    private final RestTemplate restTemplate;

    @Value("${song.service.url}")
    private String songServiceUrl;

    public void createSongMetadata(SongDto songDto) {
        try {
            restTemplate.postForObject(songServiceUrl, songDto, SongDto.class);
        } catch (Exception e) {
            log.error("Error calling Song Service to create metadata: {}. songDto={}", e.getMessage(), songDto, e);
            throw new RuntimeException("Failed to send song metadata to Song Service", e);
        }
    }

    public void deleteSongMetadata(String ids) {
        try {
            var url = UriComponentsBuilder.fromHttpUrl(songServiceUrl)
                    .queryParam("id", ids)
                    .toUriString();
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Error calling Song Service to delete metadata: {}. ids={}", e.getMessage(), ids, e);
            throw new RuntimeException("Failed to send delete request to Song Service", e);
        }
    }
}
