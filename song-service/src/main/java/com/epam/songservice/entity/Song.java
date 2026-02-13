package com.epam.songservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Long id;

    private String name;

    private String artist;

    private String album;

    private String duration;

    private String year;
}
