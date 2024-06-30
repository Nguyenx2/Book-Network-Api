package com.example.bookapi.service;

import com.example.bookapi.dto.request.user.ChangePasswordRequest;
import com.example.bookapi.dto.request.user.UpdateUserRequest;
import org.springframework.security.core.Authentication;

public interface UserService {
    Long updateUser(Long id, UpdateUserRequest request, Authentication connectedUser);

    Long lockUser(Long id);

    void changePassword(ChangePasswordRequest request, Authentication connectedUser);
}
