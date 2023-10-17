package com.example.courseworktest.core;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface VideoCompressor {
    Path compress(MultipartFile video, String userSessionId, String outputVideoPath) throws InterruptedException;
}
