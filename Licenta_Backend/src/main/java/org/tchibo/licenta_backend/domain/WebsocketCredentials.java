package org.tchibo.licenta_backend.domain;

import lombok.Getter;

@Getter
public class WebsocketCredentials {

    private final String token;

    public WebsocketCredentials(String token) {
        this.token = token;
    }
}
