package com.epam.songservice.controller;

import com.epam.songservice.dto.CreateSongResponseDto;
import com.epam.songservice.dto.DeleteSongResponseDto;
import com.epam.songservice.dto.SongRequestDto;
import com.epam.songservice.dto.SongResponseDto;
import com.epam.songservice.service.SongService;
import com.epam.songservice.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CreateSongResponseDto createSong(@Validated(ValidationSequence.class) @RequestBody SongRequestDto songRequestDto) {
        log.info("createSong: songRequestDto={}", songRequestDto);
        var id = songService.saveSong(songRequestDto);
        return new CreateSongResponseDto(id);
    }

    @GetMapping("/{id}")
    public SongResponseDto getSong(@PathVariable String id) {
        log.info("getSong: id={}", id);
        return songService.getSong(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public DeleteSongResponseDto deleteSongs(@RequestParam String id) {
        log.info("deleteSongs: id='{}'", id);
        var deletedIds = songService.deleteSongs(id);
        return new DeleteSongResponseDto(deletedIds);
    }
}
