package org.tchibo.licenta_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tchibo.licenta_backend.domain.SimpleNotificationInfo;
import org.tchibo.licenta_backend.service.NotificationService;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-notification")
    public String sendNotification(@RequestBody SimpleNotificationInfo simpleNotificationInfo) {

        try {
            notificationService.sendNotification(simpleNotificationInfo);
            return "Notification sent";
        } catch (Exception e) {
              e.printStackTrace();
              return "Exception thrown when trying to send notification: " + e.getMessage();
        }
    }
}
