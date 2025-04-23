package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FCMService {

    private final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/first-notification-4a609/messages:send";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();

 // Assuming you're using OkHttp to send the request

    public void sendNotification(String token, String title, String body) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new Exception("FCM Token is invalid.");
        }

        String accessToken = GoogleAccessToken.getAccessToken(); // Your access token method

        // Notification visible part
        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);

        // Optional: extra data payload (IMPORTANT for foreground + background custom logic)
        Map<String, String> data = new HashMap<>();
        data.put("click_action", "FLUTTER_NOTIFICATION_CLICK"); // for mobile compatibility
        data.put("customMessage", "This is extra data from server");
        data.put("title", title);  // optional duplicate for safety
        data.put("body", body);

        Map<String, Object> message = new HashMap<>();
        message.put("token", token);
        message.put("notification", notification); // for default UI rendering
        message.put("data", data);                // for background/custom processing

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", message);

        RequestBody bodyReq = RequestBody.create(JSON, objectMapper.writeValueAsString(requestBody));
        Request request = new Request.Builder()
                .url(FCM_API_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(bodyReq)
                .build();

        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code();
            String responseBody = response.body().string();

            System.out.println("FCM Status Code: " + statusCode);
            System.out.println("FCM Response Body: " + responseBody);

            if (responseBody.contains("UNREGISTERED") || responseBody.contains("Invalid registration")) {
                System.err.println("Invalid or expired FCM token! Please ensure the token is correct.");
            }

            if (!response.isSuccessful()) {
                System.err.println("Error sending notification, check response: " + responseBody);
            } else {
                System.out.println("Notification sent successfully to token.");
            }
        } catch (Exception e) {
            System.err.println(" Exception while sending FCM notification: " + e.getMessage());
        }
    }
}