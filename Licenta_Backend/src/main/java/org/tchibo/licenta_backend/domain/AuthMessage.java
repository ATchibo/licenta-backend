package org.tchibo.licenta_backend.domain;

import lombok.Getter;

@Getter
public class AuthMessage {
    private final String token;
    private final String email;

    public AuthMessage(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
