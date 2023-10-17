package com.example.courseworktest.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class VideoUploadHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> activeSessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        activeSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        activeSessions.remove(session);
    }

    public void sendProgress(int progress) throws IOException {
        for (WebSocketSession session : activeSessions) {
            if (session.isOpen()) {
                System.out.println("Session: " + session.getId() + session.getUri());
                    session.sendMessage(new TextMessage(String.valueOf(progress)));
            }
        }
    }
}
