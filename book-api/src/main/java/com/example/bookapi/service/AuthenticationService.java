package com.example.bookapi.service;

import com.example.bookapi.dto.request.AuthenticationRequest;
import com.example.bookapi.dto.request.RegistrationRequest;
import com.example.bookapi.dto.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token) throws MessagingException;

    void resendActivationEmail(String email) throws MessagingException;
}
