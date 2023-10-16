package com.example.courseworktest.controllers;

import com.example.courseworktest.entity.service.ServiceVideo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    /*@PostMapping("/addVideo")
    public String addVideo(@RequestParam("video") MultipartFile video, Model model, WebSocketSession session) {
        try {
            System.out.println("Video size: " + video.getSize());
            CompressVideo compressVideo = new CompressVideo();
            serviceVideo.createVideo(compressVideo.compressVideo(video, session));
            System.out.println("Video added");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "index";
    }*/


}
