package com.epam.resourceservice.client;

import com.epam.resourceservice.dto.SongDto;
import com.epam.resourceservice.exception.SongServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
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
            restTemplate.postForObject(songServiceUrl, songDto, Void.class);
            log.info("Successfully sent metadata to Song Service for ID: {}", songDto.getId());
        } catch (RestClientException e) {
            var errorMessage = "Failed to send song metadata to Song Service";
            log.error("{}: {}. songDto={}", errorMessage, e.getMessage(), songDto);
            throw new SongServiceUnavailableException(errorMessage, e);
        }
    }

    public void deleteSongMetadata(String ids) {
        try {
            var url = UriComponentsBuilder.fromHttpUrl(songServiceUrl)
                    .queryParam("id", ids)
                    .toUriString();
            restTemplate.delete(url);
            log.info("Successfully sent delete request to Song Service for IDs: {}", ids);
        } catch (RestClientException e) {
            var errorMessage = "Failed to send delete request to Song Service";
            log.error("{}: {}. ids={}", errorMessage, e.getMessage(), ids);
            throw new SongServiceUnavailableException(errorMessage, e);
        }
    }
}
