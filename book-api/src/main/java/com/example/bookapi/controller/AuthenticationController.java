package com.example.bookapi.controller;

import com.example.bookapi.dto.request.AuthenticationRequest;
import com.example.bookapi.dto.request.RegistrationRequest;
import com.example.bookapi.dto.response.AuthenticationResponse;
import com.example.bookapi.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        authenticationService.activateAccount(token);
    }

    @PostMapping("/resend-activation-email")
    public void resendActivationEmail(
            @RequestParam String email
    ) throws MessagingException {
        authenticationService.resendActivationEmail(email);
    }
}
