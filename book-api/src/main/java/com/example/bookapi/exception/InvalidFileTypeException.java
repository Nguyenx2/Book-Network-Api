package com.example.bookapi.exception;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String s) {
        super(s);
    }
}
