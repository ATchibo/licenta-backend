package org.tchibo.licenta_backend.controller;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tchibo.licenta_backend.domain.NotificationInfo;
import org.tchibo.licenta_backend.service.NotificationService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-notification")
    public String sendNotification(@RequestBody NotificationInfo notificationInfo) {

        try {
            notificationService.sendNotification(notificationInfo);
            return "Notification sent";
        } catch (Exception e) {
              e.printStackTrace();
              return "Error sending notification: " + e.getMessage();
        }
    }
}
