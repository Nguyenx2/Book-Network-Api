package com.example.bookapi.service.impl;

import com.example.bookapi.dto.request.user.ChangePasswordRequest;
import com.example.bookapi.dto.request.user.UpdateUserRequest;
import com.example.bookapi.entity.User;
import com.example.bookapi.exception.IncorrectPasswordException;
import com.example.bookapi.exception.OperationNotPermittedException;
import com.example.bookapi.exception.PasswordNotMatchException;
import com.example.bookapi.repository.UserRepository;
import com.example.bookapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long updateUser(Long id, UpdateUserRequest request, Authentication connectedUser) {
        User optionalUser = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user with "));
        if (!optionalUser.getId().equals(id)) {
            throw new OperationNotPermittedException("You are not allowed to update this user");
        }
        if (request.getFirstname() != null) {
            optionalUser.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            optionalUser.setLastname(request.getLastname());
        }
        if (request.getDateOfBirth() != null) {
            optionalUser.setDateOfBirth(request.getDateOfBirth());
        }
        return userRepository.save(optionalUser).getId();
    }

    @Override
    @Transactional
    public Long lockUser(Long id) {
        User user = findById(id);
        user.setAccountLocked(!user.isAccountLocked());
        return userRepository.save(user).getId();
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Old password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordNotMatchException("New password and confirm new password do not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user with id " + id));
    }
}
