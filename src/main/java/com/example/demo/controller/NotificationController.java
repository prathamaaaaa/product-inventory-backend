package com.example.demo.controller;

import com.example.demo.model.FCMRequest;
import com.example.demo.service.FCMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final FCMService fcmService;

    // Inject the FCMService
    public NotificationController(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody FCMRequest request) {
        try {
        	
            fcmService.sendNotification(request.getToken(), request.getTitle(), request.getBody());
            System.out.println(request.getToken()+"tokenfff");
            System.out.println(request.getBody()+"bodey");
            System.out.println(request.getTitle()+"title");

            return ResponseEntity.ok("Notification sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending notification: " + e.getMessage());
        }
    }
    
    }

