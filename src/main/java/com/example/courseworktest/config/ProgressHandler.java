package com.example.courseworktest.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

public class ProgressHandler extends TextWebSocketHandler {

    // List to keep track of active sessions
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // This can be expanded to handle any incoming messages if necessary
    }

    public void sendProgressUpdate(double percentage) {
        // Send progress update to all connected clients
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage("{\"percentage\":" + percentage + "}"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
