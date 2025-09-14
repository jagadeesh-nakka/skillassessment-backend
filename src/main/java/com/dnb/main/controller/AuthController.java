// src/main/java/com/dnb/main/controller/AuthController.java
package com.dnb.main.controller;

import com.dnb.main.dto.ApiResponse;
import com.dnb.main.dto.LoginRequest;
import com.dnb.main.dto.RegisterRequest;
import com.dnb.main.model.User;
import com.dnb.main.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // Check if employee ID already exists
        if (userService.existsByEmployeeId(registerRequest.getEmployeeId())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Employee ID is already registered"));
        }
        
        // Check if email already exists
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email is already registered"));
        }
        
        // Create new user
        User user = new User();
        user.setEmployeeId(registerRequest.getEmployeeId());
        user.setUsername(registerRequest.getEmail().split("@")[0]); // Use email prefix as username
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword()); // In production, encrypt this
        
        User savedUser = userService.createUser(user);
        
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", savedUser));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // For now, simple authentication without security
        // This will be replaced with JWT authentication later
        
        User user = userService.getUserByUsername(loginRequest.getUsername())
                .orElse(userService.getUserByEmail(loginRequest.getUsername()).orElse(null));
        
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        
        // Simple password check (without encryption for now)
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid credentials"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Login successful", user));
    }


@GetMapping("/current-user")
public ResponseEntity<ApiResponse> getCurrentUser(@RequestParam String username) {
    if (username == null || username.isEmpty()) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Username is required"));
    }

    User user = userService.getUserByUsername(username)
            .orElse(userService.getUserByEmail(username).orElse(null));

    if (user == null) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("User not found"));
    }

    return ResponseEntity.ok(ApiResponse.success("Current user fetched successfully", user));
}

}