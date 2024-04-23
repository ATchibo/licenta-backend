package org.tchibo.licenta_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tchibo.licenta_backend.domain.QrRequest;
import org.tchibo.licenta_backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/request-qr-info/{code}")
    public ResponseEntity<String> requestQrInfo(@PathVariable String code) {
        String token = authService.generateToken(code);
        return sendTokenInfo(token);
    }

    @PostMapping("/request-login/{code}")
    public ResponseEntity<String> requireLogin(@PathVariable String code) {
        String token = authService.generateLongLivedToken(code);
        return sendTokenInfo(token);
    }

    private ResponseEntity<String> sendTokenInfo(String token) {
        long expirationTime = authService.getExpirationTime(token);

        authService.createWebSocketSession(token);

        QrRequest qrRequest = new QrRequest(token, expirationTime);
        try {
            String response = new ObjectMapper().writeValueAsString(qrRequest);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
