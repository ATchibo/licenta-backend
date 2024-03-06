package org.tchibo.licenta_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;


@Configuration
public class FirebaseInitialization {

    @PostConstruct
    public void initializeFirebase() {
        FileInputStream serviceAccount =
                null;
        try {
//            serviceAccount = new FileInputStream("./service_account_key.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("licenta-8dee0")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
