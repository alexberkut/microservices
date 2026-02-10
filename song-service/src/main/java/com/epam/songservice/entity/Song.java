package com.epam.songservice.entity;

import com.epam.songservice.validation.First;
import com.epam.songservice.validation.Second;
import com.epam.songservice.validation.ValidYear;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "song")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
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
    @Pattern(groups = Second.class, regexp = "[0-5][0-9]:[0-5][0-9]", message = "Duration must be in mm:ss format with leading zeros")
    private String duration;

    @NotNull(groups = First.class, message = "Year is required")
    @ValidYear(groups = Second.class)
    private String year;
}
