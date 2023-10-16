package com.example.courseworktest;

import jakarta.persistence.*;

import java.nio.file.Path;

@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String pathToVideo;


    public Video() {
    }

    public Video(String pathToVideo) {
        this.pathToVideo = pathToVideo;
    }

    public String getPathToVideo() {
        return pathToVideo;
    }

    public void setPathToVideo(String pathToVideo) {
        this.pathToVideo = pathToVideo;
    }

    public Long getId() {
        return id;
    }
}
