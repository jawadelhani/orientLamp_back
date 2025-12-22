package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.AuthRequest;
import com.example.orientlamp_back.dto.AuthResponse;
import com.example.orientlamp_back.dto.RefreshTokenRequest;
import com.example.orientlamp_back.dto.RegisterRequest;
import com.example.orientlamp_back.service.AuthService;
import com.example.orientlamp_back.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        String baseUrl = httpRequest.getScheme() + "://" +
                httpRequest.getServerName() + ":" +
                httpRequest.getServerPort();

        AuthResponse response = authService.register(request, baseUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("token") String token) {
        boolean verified = emailService.verifyEmail(token);

        Map<String, String> response = new HashMap<>();
        if (verified) {
            response.put("message", "Email verified successfully! You can now log in.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid or expired verification token.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Authentication service is running!");
        return ResponseEntity.ok(response);
    }
}
