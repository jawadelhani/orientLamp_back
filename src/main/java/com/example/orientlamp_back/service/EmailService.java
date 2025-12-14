package com.example.orientlamp_back.service;

import com.example.orientlamp_back.entity.EmailVerificationToken;
import com.example.orientlamp_back.entity.User;
import com.example.orientlamp_back.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailVerificationTokenRepository tokenRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${email.verification.expiration}")
    private long verificationExpiration;

    @Value("${email.verification.from}")
    private String fromEmail;

    @Transactional
    public void sendVerificationEmail(User user, String baseUrl) {
        // Generate token
        String token = UUID.randomUUID().toString();

        // Save token to database
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(verificationExpiration))
                .build();
        tokenRepository.save(verificationToken);

        // Store in Redis with expiration
        String redisKey = "email_verification:" + token;
        redisTemplate.opsForValue().set(redisKey, user.getEmail(), verificationExpiration, TimeUnit.SECONDS);

        // Send email
        String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + token;
        String subject = "Email Verification";
        String message = "Hello " + user.getName() + ",\n\n" +
                "Please click the link below to verify your email address:\n" +
                verificationUrl + "\n\n" +
                "This link will expire in " + (verificationExpiration / 60) + " minutes.\n\n" +
                "If you didn't create an account, please ignore this email.";

        sendEmail(user.getEmail(), subject, message);
        log.info("Verification email sent to: {}", user.getEmail());
    }

    @Transactional
    public boolean verifyEmail(String token) {
        // Check Redis first
        String redisKey = "email_verification:" + token;
        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) {
            log.warn("Token not found in Redis: {}", token);
            return false;
        }

        // Verify from database
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.isExpired()) {
            log.warn("Token expired: {}", token);
            return false;
        }

        if (verificationToken.getVerified()) {
            log.warn("Token already used: {}", token);
            return false;
        }

        // Mark as verified
        verificationToken.setVerified(true);
        tokenRepository.save(verificationToken);

        // Enable user
        User user = verificationToken.getUser();
        user.setEnabled(true);

        // Clean up Redis
        redisTemplate.delete(redisKey);

        log.info("Email verified successfully for: {}", user.getEmail());
        return true;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}