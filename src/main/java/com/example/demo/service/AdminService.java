package com.example.demo.service;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    public String registerAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return "Admin registered successfully!";
    }

    public Map<String, Object> loginAdmin(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);

        if (admin.isPresent()) {
            Admin adminData = admin.get();
            System.out.println("pass");
            
            if (passwordEncoder.matches(password, adminData.getPassword())) {
                System.out.println("success");

                String token = jwtUtil.generateToken(email);
                adminData.setToken(token);
                adminRepository.save(adminData);
                Map<String, Object> response = new HashMap<>();
                response.put("id", adminData.getId());
                response.put("name", adminData.getName());
                response.put("email", adminData.getEmail());
                response.put("token", token); 
                System.out.println("Sending response: " + response); 
                return response;
            }
        }
        return null; 
    }    
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
