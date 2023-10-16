package com.example.courseworktest;

import com.example.courseworktest.core.CompressVideo;
import com.example.courseworktest.entity.Video;
import com.example.courseworktest.entity.service.ServiceVideo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    private final ServiceVideo serviceVideo;

    public MainController(ServiceVideo serviceVideo) {
        this.serviceVideo = serviceVideo;
    }


    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("videos", serviceVideo.findAll());

        return "index";
    }

    @PostMapping("/addVideo")
    public String addVideo(@RequestParam("video") MultipartFile video, Model model) {
        try {

            System.out.println("Video size: " + video.getSize());
            serviceVideo.createVideo(Objects.requireNonNull(CompressVideo.compressVideo(video)));
            /*if (video.getSize() <= 5 * 1024 * 1024) {
                serviceVideo.createVideo(Path.of("videos/" + video.getOriginalFilename()));
                Files.write(Path.of("videos/" + video.getOriginalFilename()), video.getBytes());
            } else {
                serviceVideo.createVideo(Objects.requireNonNull(CompressVideo.compressVideo(video)));
            }*/
            System.out.println("Video added");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "index";
    }
}
