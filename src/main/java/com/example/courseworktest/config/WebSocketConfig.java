package com.example.courseworktest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    public final VideoUploadHandler videoUploadHandler;

    public WebSocketConfig(VideoUploadHandler videoUploadHandler) {
        this.videoUploadHandler = videoUploadHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(videoUploadHandler, "/video-upload-progress");
    }
}

