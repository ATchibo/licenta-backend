package org.tchibo.licenta_backend.domain;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SimpleNotificationInfo {

    private String title;
    private String body;
    private String raspberryId;
    private String data;

    public Message toNotificationMessage(String token) {
        return Message.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .putData("data", data)
                .putData("title", title)
                .putData("body", body)
                .setToken(token)
                .build();
    }

    @Override
    public String toString() {
        return "NotificationInfo{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", raspberryId='" + raspberryId + '\'' +
                '}';
    }
}
