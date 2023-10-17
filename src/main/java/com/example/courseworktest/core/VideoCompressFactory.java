package com.example.courseworktest.core;

import com.example.courseworktest.config.VideoUploadHandler;

public class VideoCompressFactory {
    public static VideoCompressor createCompressor(VideoUploadHandler videoUploadHandler) {
        return new VideoCompress(videoUploadHandler);
    }
}
