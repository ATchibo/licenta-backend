package org.tchibo.licenta_backend.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.stereotype.Service;
import org.tchibo.licenta_backend.domain.SimpleNotificationInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    private final Firestore database;
    private final FirebaseMessaging firebaseMessaging;

    private final String OWNER_INFO = "owner_info";

    public FirebaseService() {
        this.database = FirestoreClient.getFirestore();
        this.firebaseMessaging = FirebaseMessaging.getInstance();
    }

    public void sendNotification(List<String> ownerFcmTokens, SimpleNotificationInfo simpleNotificationInfo) throws Exception {
        try {
            for (String token : ownerFcmTokens) {
                firebaseMessaging.send(simpleNotificationInfo.toNotificationMessage(token));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error sending notification: " + e.getMessage());
        }
    }

    public String getOwnerEmail(String raspberryId) {
        try {
            QuerySnapshot snapshot = database.collection(OWNER_INFO)
                    .whereArrayContains("raspberry_ids", raspberryId)
                    .get()
                    .get();

            if (snapshot.isEmpty()) {
                return null;
            }

            if (snapshot.size() > 1) {
                throw new RuntimeException("Multiple owners found");
            }

            return snapshot.getDocuments().getFirst().getId();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getOwnerFcmTokens(String ownerEmail) {
        try {
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) database.collection(OWNER_INFO)
                    .document(ownerEmail)
                    .get()
                    .get()
                    .get("tokens");

            return list;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
