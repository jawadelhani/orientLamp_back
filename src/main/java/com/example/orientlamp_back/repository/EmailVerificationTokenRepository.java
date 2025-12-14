package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.EmailVerificationToken;
import com.example.orientlamp_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findByUser(User user);
    void deleteByExpiryDateBefore(LocalDateTime now);
    void deleteByUser(User user);
}