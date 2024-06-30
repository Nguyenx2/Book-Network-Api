package com.example.bookapi.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank(message = "Old password must not be empty")
    @JsonProperty("old_password")
    private String oldPassword;
    @NotBlank(message = "New password must not be empty")
    @JsonProperty("new_password")
    private String newPassword;
    @NotBlank(message = "Confirm password must not be empty")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
