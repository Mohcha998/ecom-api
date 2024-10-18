package com.ecom.controller;

import com.ecom.models.entities.User;
import com.ecom.services.UserService;
import com.ecom.utils.JwtUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        System.out.println("Login attempt for user: " + user.getUsername());
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for user: " + user.getUsername() + ". Error: " + e.getMessage());
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        // Memastikan format "Bearer <token>"
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(403).body(null);
        }

        // Mengambil token dari header
        String actualToken = token.substring(7); // Hapus "Bearer "
        String username = jwtUtil.extractUsername(actualToken);
        
        // Debugging untuk melihat username
        System.out.println("Extracted username: " + username);

        // Ambil user dari service menggunakan username
        User user = userService.findByUsername(username);
        
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }


}

