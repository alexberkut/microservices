package com.epam.songservice.mapper;

import com.epam.songservice.dto.SongRequestDto;
import com.epam.songservice.dto.SongResponseDto;
import com.epam.songservice.entity.Song;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {

    public Song toEntity(SongRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new Song(
                dto.getId(),
                dto.getName(),
                dto.getArtist(),
                dto.getAlbum(),
                dto.getDuration(),
                dto.getYear()
        );
    }

    public SongResponseDto toResponseDto(Song entity) {
        if (entity == null) {
            return null;
        }
        return SongResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .artist(entity.getArtist())
                .album(entity.getAlbum())
                .duration(entity.getDuration())
                .year(entity.getYear())
                .build();
    }
}
