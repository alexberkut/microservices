package com.epam.songservice.service;

import com.epam.songservice.dto.SongRequestDto;
import com.epam.songservice.dto.SongResponseDto;
import com.epam.songservice.exception.SongNotFoundException;
import com.epam.songservice.mapper.SongMapper;
import com.epam.songservice.repository.SongRepository;
import com.epam.songservice.validation.SongValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final SongValidator songValidator;

    @Transactional
    public Long saveSong(SongRequestDto songDto) {
        log.info("saveSong: songDto={}", songDto);
        if (songRepository.existsById(songDto.getId())) {
            throw new IllegalStateException("Metadata for resource ID=%d already exists".formatted(songDto.getId()));
        }
        var song = songMapper.toEntity(songDto);
        var savedSong = songRepository.save(song);
        return savedSong.getId();
    }

    public SongResponseDto getSong(String id) {
        songValidator.validateId(id);
        log.info("getSong: id={}", id);
        var songId = Long.parseLong(id);
        var song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException(String.format("Song metadata for ID=%d not found", songId)));
        return songMapper.toResponseDto(song);
    }

    @Transactional
    public List<Long> deleteSongs(String ids) {
        songValidator.validateIds(ids);
        log.info("deleteSongs: ids='{}'", ids);
        
        var idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        var songsToDelete = songRepository.findAllById(idList);
        var deletedIds = songsToDelete.stream().map(s -> s.getId()).toList();

        if (!deletedIds.isEmpty()) {
            songRepository.deleteAll(songsToDelete);
            log.info("Successfully deleted songs with IDs: {}", deletedIds);
        }

        return deletedIds;
    }
}
