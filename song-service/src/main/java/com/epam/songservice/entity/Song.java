package com.epam.songservice.entity;

import com.epam.songservice.validation.First;
import com.epam.songservice.validation.Second;
import com.epam.songservice.validation.ValidDuration;
import com.epam.songservice.validation.ValidYear;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @NotNull(groups = First.class)
    private Long id;

    @NotNull(groups = First.class)
    @Size(min = 1, max = 100, groups = Second.class)
    private String name;

    @NotNull(groups = First.class)
    @Size(min = 1, max = 100, groups = Second.class)
    private String artist;

    @NotNull(groups = First.class)
    @Size(min = 1, max = 100, groups = Second.class)
    private String album;

    @NotNull(groups = First.class)
    @ValidDuration(groups = Second.class)
    private String duration;

    @NotNull(groups = First.class)
    @ValidYear(groups = Second.class)
    private String year;
}
