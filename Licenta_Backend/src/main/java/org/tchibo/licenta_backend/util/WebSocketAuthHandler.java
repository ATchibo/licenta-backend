package org.tchibo.licenta_backend.util;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketAuthHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        String receivedMessage = (String) message.getPayload();

        if (sessions.size() != 2) {
            return;
        }

        for (WebSocketSession s : sessions) {
            if (s != session) {
                s.sendMessage(new TextMessage("Message from server: " + receivedMessage));
            }
        }
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        if (sessions.size() == 2) {
            session.close();
            return;
        }

        sessions.add(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
