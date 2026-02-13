package com.epam.resourceservice.controller;

import com.epam.resourceservice.dto.DeleteResourceResponseDto;
import com.epam.resourceservice.dto.UploadResourceResponseDto;
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

@RestController
@RequestMapping(value = "/resources")
@Slf4j
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UploadResourceResponseDto uploadResource(@RequestBody byte[] data) {
        log.info("uploadResource: data=[{} bytes]", data.length);
        var id = resourceService.saveResource(data);
        return new UploadResourceResponseDto(id);
    }

    @GetMapping(value = "/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getResource(@PathVariable String id) {
        log.info("getResource: id={}", id);
        var resource = resourceService.getResource(id);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        return new ResponseEntity<>(resource.getData(), headers, HttpStatus.OK);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeleteResourceResponseDto deleteResources(@RequestParam String id) {
        log.info("deleteResources: id='{}'", id);
        var deletedIds = resourceService.deleteResources(id);
        return new DeleteResourceResponseDto(deletedIds);
    }
}
