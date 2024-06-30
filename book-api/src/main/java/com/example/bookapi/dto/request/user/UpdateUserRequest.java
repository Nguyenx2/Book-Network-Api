package com.example.bookapi.dto.request.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
}
