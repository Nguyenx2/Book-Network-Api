package com.example.bookapi.exception;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String s) {
        super(s);
    }
}
