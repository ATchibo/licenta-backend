package org.tchibo.licenta_backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.tchibo.licenta_backend.domain.AuthMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WebSocketAuthHandler extends TextWebSocketHandler {

    static class SessionEntry {
        public WebSocketSession firstDeviceSession = null;
        public WebSocketSession secondDeviceSession = null;
    }
    private final Map<String, SessionEntry> availableEntries =
            new HashMap<String, SessionEntry>();


    private String retrieveSessionId(String url) {
        String[] splitUrl = url.split("/");
        return splitUrl[splitUrl.length - 1];
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String url = session.getUri().toString();
        String sessionId = retrieveSessionId(url);

        SessionEntry sessionEntry = availableEntries.get(sessionId);
        if (sessionEntry != null) {
            if (sessionEntry.firstDeviceSession == null) {
                sessionEntry.firstDeviceSession = session;
            }
            else if (sessionEntry.secondDeviceSession == null) {
                sessionEntry.secondDeviceSession = session;
            }
            else {
                session.close(CloseStatus.NORMAL.withReason("Session count exceeded"));
            }
        }
        else {
            session.close(CloseStatus.NORMAL.withReason("Invalid session"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String url = session.getUri().toString();
        String sessionId = retrieveSessionId(url);
        SessionEntry sessionEntry = availableEntries.get(sessionId);
        if (sessionEntry != null) {
            if (sessionEntry.firstDeviceSession == session) {
                sessionEntry.firstDeviceSession = null;
            }
            else if (sessionEntry.secondDeviceSession == session) {
                sessionEntry.secondDeviceSession = null;
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String url = session.getUri().toString();
        String sessionId = retrieveSessionId(url);
        SessionEntry sessionEntry = availableEntries.get(sessionId);

        if (sessionEntry != null) {
            WebSocketSession otherSession = (session == sessionEntry.firstDeviceSession) ?
                    sessionEntry.secondDeviceSession : sessionEntry.firstDeviceSession;
            if (otherSession != null) {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode node = objectMapper.readTree(message.getPayload());

//                    if (node.get("uid") != null && node.get("email") != null) {
//                        String uid = node.get("uid").asText();
                    if (node.get("email") != null) {
                        String uid = UUID.randomUUID().toString();

                        String newToken = FirebaseAuth.getInstance().createCustomToken(uid);
                        AuthMessage authMessage = new AuthMessage(newToken, node.get("email").asText());

                        TextMessage newMessage = new TextMessage(objectMapper.writeValueAsString(authMessage));
                        otherSession.sendMessage(newMessage);
                    } else {
                        otherSession.sendMessage(message);
                    }
                } catch (Exception e) {
                    otherSession.sendMessage(message);
                }
            }
        }
    }

    public void endSession(String sessionId) {
        SessionEntry sessionEntry = availableEntries.get(sessionId);
        if (sessionEntry != null) {
            if (sessionEntry.firstDeviceSession != null) {
                try {
                    sessionEntry.firstDeviceSession.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sessionEntry.secondDeviceSession != null) {
                try {
                    sessionEntry.secondDeviceSession.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            availableEntries.remove(sessionId);
        }
    }

    public void createSession(String token) {
        availableEntries.putIfAbsent(token, new SessionEntry());
    }
}
