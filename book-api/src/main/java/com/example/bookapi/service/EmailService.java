package com.example.bookapi.service;

import com.example.bookapi.entity.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject) throws MessagingException;
}
