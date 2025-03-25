package com.example.demo.controller;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.GoogleAuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") 
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;
    
    @Autowired
	private JwtUtil jwtUtil;

    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        String idToken = request.get("token");
        Map<String, Object> adminData = googleAuthService.verifyGoogleToken(idToken);

        if (adminData != null) {
            return ResponseEntity.ok(adminData); 
        } else {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid Google token");
        }
    }
}
