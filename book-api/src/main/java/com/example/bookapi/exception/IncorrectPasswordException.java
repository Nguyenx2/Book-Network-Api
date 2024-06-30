package com.example.bookapi.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String s) {
        super(s);
    }
}
