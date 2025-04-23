package com.example.demo.service;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;

public class GoogleAccessToken {

    public static String getAccessToken() throws Exception {
        // Adjust file path as needed
        String serviceAccountPath = "src/main/resources/firebase-service-account.json"; // Can be configured
        File file = new File("src/main/resources/firebase-service-account.json");
        System.out.println("File exists: " + file.exists());

        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-service-account.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/firebase.messaging"));

        credentials.refreshIfExpired();

        return credentials.getAccessToken().getTokenValue();
    }
}
