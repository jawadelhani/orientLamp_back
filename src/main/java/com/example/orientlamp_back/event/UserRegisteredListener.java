package com.example.orientlamp_back.event;

import com.example.orientlamp_back.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
        try {
            emailService.sendVerificationEmailUsingToken(event.user(), event.token(), event.baseUrl());
        } catch (Exception ex) {
            log.warn("Exception while sending verification email for {}: {}", event.user().getEmail(), ex.getMessage());
        }
    }
}
