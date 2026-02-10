package com.epam.songservice.controller;

import com.epam.songservice.dto.CreateSongResponseDto;
import com.epam.songservice.dto.DeleteSongResponseDto;
import com.epam.songservice.entity.Song;
import com.epam.songservice.exception.SongNotFoundException;
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

import java.util.Arrays;

@RestController
@RequestMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CreateSongResponseDto createSong(@Validated(ValidationSequence.class) @RequestBody Song song) {
        log.info("createSong: song={}", song);
        var id = songService.saveSong(song);
        if (id == null) {
            throw new IllegalStateException("Metadata for resource ID=%d already exists".formatted(song.getId()));
        }
        return new CreateSongResponseDto(id);
    }

    @GetMapping("/{id}")
    public Song getSong(@PathVariable String id) {
        log.info("getSong: id={}", id);
        long songId;
        try {
            songId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value '%s' for ID. Must be a positive integer".formatted(id));
        }
        if (songId <= 0) {
            throw new IllegalArgumentException("Invalid value '%s' for ID. Must be a positive integer".formatted(id));
        }

        var song = songService.getSong(songId);
        if (song == null) {
            throw new SongNotFoundException(String.format("Song metadata for ID=%s not found", songId));
        }
        return song;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public DeleteSongResponseDto deleteSongs(@RequestParam String id) {
        log.info("deleteSongs: id={}", id);
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

        var deletedIds = songService.deleteSongs(id);
        return new DeleteSongResponseDto(deletedIds);
    }
}
