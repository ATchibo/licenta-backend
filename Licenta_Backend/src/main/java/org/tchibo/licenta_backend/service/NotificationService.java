package org.tchibo.licenta_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tchibo.licenta_backend.domain.SimpleNotificationInfo;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private FirebaseService firebaseService;

    public void sendNotification(SimpleNotificationInfo simpleNotificationInfo) throws Exception {
        String ownerEmail = firebaseService.getOwnerEmail(simpleNotificationInfo.getRaspberryId());

        if (ownerEmail == null) {
            throw new Exception("Owner not found");
        }

        List<String> ownerFcmTokens = firebaseService.getOwnerFcmTokens(ownerEmail);

        if (ownerFcmTokens == null || ownerFcmTokens.isEmpty()) {
            return;
        }

        firebaseService.sendNotification(ownerFcmTokens, simpleNotificationInfo);
    }
}
