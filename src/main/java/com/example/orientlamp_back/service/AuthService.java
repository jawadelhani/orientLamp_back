package com.example.orientlamp_back.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import com.example.orientlamp_back.dto.AuthRequest;
import com.example.orientlamp_back.dto.AuthResponse;
import com.example.orientlamp_back.dto.RegisterRequest;
import com.example.orientlamp_back.entity.User;
import com.example.orientlamp_back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AuthResponse register(RegisterRequest request, String baseUrl) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new com.example.orientlamp_back.exception.EmailAlreadyExistsException("This email already exists");
        }

        // Create new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false) // Disabled until email verification
                .build();


                // Set current study level if provided
                if (request.getCurrentStudyLevel() != null) {
                    user.setCurrentStudyLevel(request.getCurrentStudyLevel());
                }

                userRepository.save(user);

                // Create verification token and publish event AFTER COMMIT to send email
                String token = emailService.createVerificationToken(user);
                eventPublisher.publishEvent(new com.example.orientlamp_back.event.UserRegisteredEvent(user, token, baseUrl));

        // Do not generate tokens at registration. Require email verification first.
        return AuthResponse.builder()
            .accessToken(null)
            .refreshToken(null)
            .email(user.getEmail())
            .name(user.getFirstName() + " " + user.getLastName())
            .message("Registration successful. Please check your email to verify your account.")
            .build();
    }

    public AuthResponse login(AuthRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Get user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if email is verified
        if (!user.isEnabled()) {
            throw new RuntimeException("Email not verified. Please verify your email first.");
        }

        // Generate tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .message("Login successful")
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        String userEmail = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .message("Token refreshed successfully")
                .build();
    }
}