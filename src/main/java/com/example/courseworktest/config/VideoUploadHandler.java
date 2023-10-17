package com.example.courseworktest.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class VideoUploadHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> activeSessions = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        activeSessions.add(session);
        userSessionMap.put(session.getId(), session);
        String sessionId = "SESSION_ID:" + session.getId();
        session.sendMessage(new TextMessage(sessionId));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        activeSessions.remove(session);
        userSessionMap.values().remove(session);
    }

    public void sendProgress(int progress, String userSessionId) throws IOException {
        /*for (WebSocketSession session : activeSessions) {
            if (session.isOpen()) {
                System.out.println("Session: " + session.getId() + session.getUri());
                    session.sendMessage(new TextMessage(String.valueOf(progress)));
            }
        }*/
        WebSocketSession userSession = userSessionMap.get(userSessionId);
        if(userSession != null && userSession.isOpen()) {
            userSession.sendMessage(new TextMessage("PROGRESS:" + progress));
        }
    }

    public void sendMessage(String message, String userSessionId) throws IOException {
        WebSocketSession userSession = userSessionMap.get(userSessionId);
        if(userSession != null && userSession.isOpen()) {
            userSession.sendMessage(new TextMessage(message));
        }
    }

   /* public void sendProgressUser(int progress) throws IOException {
        if (userUploadingSession != null && userUploadingSession.isOpen()) {
            System.out.println("Session: " + userUploadingSession.getId() + userUploadingSession.getUri());
            userUploadingSession.sendMessage(new TextMessage(String.valueOf(progress)));
        }
    }*/
}
