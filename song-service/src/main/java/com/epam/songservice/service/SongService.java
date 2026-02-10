package com.epam.songservice.service;

import com.epam.songservice.entity.Song;
import com.epam.songservice.repository.SongRepository;
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

    @Transactional
    public Long saveSong(Song song) {
        if (songRepository.existsById(song.getId())) {
            log.warn("Song already exists - saveSong: song={}", song);
            return null;
        }
        var savedSong = songRepository.save(song);
        return savedSong.getId();
    }

    public Song getSong(Long id) {
        return songRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Long> deleteSongs(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();

        // Find which of the requested IDs actually exist
        List<Song> songsToDelete = songRepository.findAllById(idList);
        List<Long> deletedIds = songsToDelete.stream()
                .map(Song::getId)
                .toList();
        
        if (deletedIds.isEmpty()) {
            log.info("No songs found for the given IDs. Nothing to delete.");
            return List.of();
        }

        // Delete only the songs that were found
        songRepository.deleteAll(songsToDelete);

        log.info("Successfully deleted songs with IDs: {}", deletedIds);
        return deletedIds;
    }
}
