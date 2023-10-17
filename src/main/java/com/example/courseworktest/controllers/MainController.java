package com.example.courseworktest.controllers;

import com.example.courseworktest.config.VideoUploadHandler;
import com.example.courseworktest.core.CompressVideo;
import com.example.courseworktest.entity.service.ServiceVideo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.WebSocketSession;

@Controller
public class MainController {

    private final ServiceVideo serviceVideo;
    private final VideoUploadHandler videoUploadHandler;


    public MainController(ServiceVideo serviceVideo, VideoUploadHandler videoUploadHandler) {
        this.serviceVideo = serviceVideo;
        this.videoUploadHandler = videoUploadHandler;
    }


    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("videos", serviceVideo.findAll());

        return "index";
    }

    @PostMapping("/addVideo")
    public String addVideo(@RequestParam("video") MultipartFile video, RedirectAttributes redirectAttributes) {
        if (video.isEmpty()) {
            redirectAttributes.addFlashAttribute("name", "Video not selected");
        } else {
            try {
                CompressVideo compressVideo = new CompressVideo(videoUploadHandler);

                serviceVideo.createVideo(compressVideo.compressVideo(video));
                redirectAttributes.addFlashAttribute("name", "Video " + video.getOriginalFilename() + " added");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return "redirect:/";
    }


}
