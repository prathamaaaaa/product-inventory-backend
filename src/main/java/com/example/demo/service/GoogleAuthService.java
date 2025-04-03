package com.example.demo.service;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Service
public class GoogleAuthService {

    private static final String CLIENT_ID = "45809495699-dbklp080vhk4il0uuoko2951o48jk2ku.apps.googleusercontent.com";
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(CLIENT_ID))
             .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                String email = googleIdToken.getPayload().getEmail();
                System.out.println("Google Login Successful: " + email);

                Optional<Admin> adminOpt = adminRepository.findByEmail(email);
                if (adminOpt.isPresent()) {
                    Admin adminData = adminOpt.get();
                    String token = jwtUtil.generateToken(email);
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", adminData.getId());
                    response.put("name", adminData.getName());
                    response.put("email", adminData.getEmail());
                    response.put("token", token);
                    adminData.setToken(token);
                    adminRepository.save(adminData);
                    System.out.println("Sending response: " + response);
                    return response;
                } else {
                    System.out.println("Admin not found!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
