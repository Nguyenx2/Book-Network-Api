package com.example.bookapi.exception.advice;

import lombok.Getter;

public enum ErrorCodes {

    NO_CODE(0, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(302, "The new password does not match"),
    ACCOUNT_LOCKED(302, "User account is locked"),
    ACCOUNT_DISABLED(303, "User account is disabled"),
    BAD_CREDENTIAL(304, "Email or password is incorrect");

    @Getter
    private final int code;
    @Getter
    private final String description;


    ErrorCodes(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
