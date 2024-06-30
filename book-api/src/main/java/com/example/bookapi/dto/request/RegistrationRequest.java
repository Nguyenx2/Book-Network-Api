package com.example.bookapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotBlank(message = "Firstname is required")
    private String firstname;

    @NotBlank(message = "Lastname is required")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not formatted")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "The password must have at least 8 characters")
    private String password;
}
