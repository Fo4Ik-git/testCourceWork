package com.example.courseworktest.entity.service;

import com.example.courseworktest.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@EnableJpaRepositories
@Repository
interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAll();

}

@Service
public class ServiceVideo {
    private final VideoRepository videoRepository;

    public ServiceVideo(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }


    public void createVideo(Video video) {
        videoRepository.save(video);
    }

    public void createVideo(Path pathToVideo) {
        Video video = new Video(pathToVideo.toString());
        videoRepository.save(video);
    }

    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    public Video findById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }


}