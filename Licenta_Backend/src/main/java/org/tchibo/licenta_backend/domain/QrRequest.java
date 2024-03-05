package org.tchibo.licenta_backend.domain;

import lombok.Getter;

@Getter
public class QrRequest {
    private String token;
    private long expirationTime;

    public QrRequest(String token, long expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }
}
