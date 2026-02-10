package com.epam.resourceservice.controller;

import com.epam.resourceservice.dto.DeleteResourceResponseDto;
import com.epam.resourceservice.dto.UploadResourceResponseDto;
import com.epam.resourceservice.exception.ResourceNotFoundException;
import com.epam.resourceservice.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/resources", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg")
    @ResponseStatus(HttpStatus.OK)
    public UploadResourceResponseDto uploadResource(@RequestBody byte[] data) {
        log.info("uploadResource: data=[{} bytes]", data.length);
        var id = resourceService.saveResource(data);
        return new UploadResourceResponseDto(id);
    }

    @GetMapping(value = "/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        log.info("getResource: id={}", id);
        if (id <= 0) {
            var msg = String.format("Invalid value '%d' for ID. Must be a positive integer", id);
            throw new IllegalArgumentException(msg);
        }

        var resource = resourceService.getResource(id);
        if (resource == null) {
            throw new ResourceNotFoundException("Resource with ID=%d not found".formatted(id));
        }
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        return new ResponseEntity<>(resource.getData(), headers, HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public DeleteResourceResponseDto deleteResources(@RequestParam String id) {
        log.info("deleteResources: id={}", id);
        if (id.length() > 200) {
            throw new IllegalArgumentException("CSV string is too long: received %d characters, maximum allowed is 200".formatted(id.length()));
        }
        Arrays.stream(id.split(","))
                .forEach(idStr -> {
                    long num;
                    try {
                        num = Long.parseLong(idStr);
                    } catch (NumberFormatException e) {
                        var msg = String.format("Invalid ID format: '%s'. Only positive integers are allowed", idStr);
                        throw new IllegalArgumentException(msg);
                    }
                    if (num <= 0) {
                        var msg = String.format("Invalid ID format: '%s'. Only positive integers are allowed", idStr);
                        throw new IllegalArgumentException(msg);
                    }
                });

        var deletedIds = resourceService.deleteResources(id);
        return new DeleteResourceResponseDto(deletedIds);
    }
}
