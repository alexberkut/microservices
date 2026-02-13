package com.epam.songservice.dto;

import com.epam.songservice.validation.First;
import com.epam.songservice.validation.Second;
import com.epam.songservice.validation.ValidDuration;
import com.epam.songservice.validation.ValidYear;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongRequestDto {

    @NotNull(groups = First.class)
    private Long id;

    @NotNull(groups = First.class, message = "Song name is required")
    @Size(min = 1, max = 100, groups = Second.class, message = "Song name must be between 1 and 100 characters")
    private String name;

    @NotNull(groups = First.class, message = "Artist name is required")
    @Size(min = 1, max = 100, groups = Second.class, message = "Artist name must be between 1 and 100 characters")
    private String artist;

    @NotNull(groups = First.class, message = "Album name is required")
    @Size(min = 1, max = 100, groups = Second.class, message = "Album name must be between 1 and 100 characters")
    private String album;

    @NotNull(groups = First.class, message = "Duration is required")
    @ValidDuration(groups = Second.class)
    private String duration;

    @NotNull(groups = First.class, message = "Year is required")
    @ValidYear(groups = Second.class)
    private String year;
}
