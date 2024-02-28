package org.tchibo.licenta_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tchibo.licenta_backend.domain.NotificationInfo;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private FirebaseService firebaseService;

    // TODO: custom exception

    public void sendNotification(NotificationInfo notificationInfo) throws Exception {
        String ownerEmail = firebaseService.getOwnerEmail(notificationInfo.getRaspberryId());

        if (ownerEmail == null) {
            throw new Exception("Owner not found");
        }

        List<String> ownerFcmTokens = firebaseService.getOwnerFcmTokens(ownerEmail);

        if (ownerFcmTokens == null || ownerFcmTokens.isEmpty()) {
            return;
        }

        firebaseService.sendNotification(ownerFcmTokens, notificationInfo);
    }
}
