package com.lib.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // Professional logging
public class EmailService {

    private final JavaMailSender mailSender;

    @Async // Runs in background thread
    public void sendWelcomeEmail(String toEmail) {
        try {
            log.info("Sending welcome email to {}", toEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@library.com");
            message.setTo(toEmail);
            message.setSubject("Welcome to the Library System");
            message.setText("Your account has been created successfully!");

            mailSender.send(message);
            log.info("Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
}
