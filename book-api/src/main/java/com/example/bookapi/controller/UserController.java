package com.example.bookapi.controller;

import com.example.bookapi.dto.request.user.ChangePasswordRequest;
import com.example.bookapi.dto.request.user.UpdateUserRequest;
import com.example.bookapi.entity.User;
import com.example.bookapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/update/{id}")
    ResponseEntity<Long> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request, connectedUser));
    }

    @PatchMapping("/lock-or-unlock/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Long> lockUser(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(userService.lockUser(id));
    }

    @GetMapping("/change-password")
    ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication connectedUser) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }
}
