package org.tchibo.licenta_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tchibo.licenta_backend.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    public String generateToken(String code) {
        return jwtUtil.generateToken(code);
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
