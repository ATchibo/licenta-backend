package org.tchibo.licenta_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.tchibo.licenta_backend.util.JwtUtil;
import org.tchibo.licenta_backend.util.WebSocketAuthHandler;

import java.time.Instant;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WebSocketAuthHandler webSocketAuthHandler;

    private ThreadPoolTaskScheduler taskScheduler;

    public AuthService() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
    }

    public String generateToken(String code) {
        return jwtUtil.generateToken(code);
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public long getExpirationTime(String token) {
        return jwtUtil.getExpirationTime(token).getTime();
    }

    public void createWebSocketSession(String token) {
        webSocketAuthHandler.createSession(token);

        taskScheduler.schedule(
                () -> endWebSocketSession(token),
                triggerContext -> jwtUtil.getExpirationTime(token).toInstant()
        );
    }

    private void endWebSocketSession(String token) {
        webSocketAuthHandler.endSession(token);
    }
}
