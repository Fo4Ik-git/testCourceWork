package com.example.courseworktest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Controller
public class MainController {

    private final ServiceVideo serviceVideo;

    public MainController(ServiceVideo serviceVideo) {
        this.serviceVideo = serviceVideo;
    }


    @GetMapping("/")
    public String main(Model model) {

        Video video = serviceVideo.findById(2L);
        if(video != null) {
            System.out.println("Video path: " + video.getPathToVideo());
            model.addAttribute("video", video.getPathToVideo());
        }


        return "index";
    }

    @PostMapping("/addVideo")
    public String addVideo(@RequestParam("video") MultipartFile video, Model model) {

        try{
//            serviceVideo.createVideo();
            //Get size of file
            System.out.println("Video size: " + video.getSize());
            //if file size < 5 mb print it
            if(video.getSize() <= 5 * 1024 * 1024){
                serviceVideo.createVideo(Path.of(video.getOriginalFilename()));
                Files.write(Path.of(video.getOriginalFilename()), video.getBytes());
            } else {
                serviceVideo.createVideo(Objects.requireNonNull(CompressVideo.compressVideo(video)));
            }

            System.out.println("Video added");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return "index";
    }


}
