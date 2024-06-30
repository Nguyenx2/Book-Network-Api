package com.example.bookapi.exception.advice;

import com.example.bookapi.exception.*;
import jakarta.mail.MessagingException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.example.bookapi.exception.advice.ErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exception) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ACCOUNT_LOCKED.getCode())
                                .errorDescription(ACCOUNT_LOCKED.getDescription())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exception) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ACCOUNT_DISABLED.getCode())
                                .errorDescription(ACCOUNT_DISABLED.getDescription())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exception) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(BAD_CREDENTIAL.getCode())
                                .errorDescription(BAD_CREDENTIAL.getDescription())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .errorDescription("Internal error, contact the admin")
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleException(IncorrectPasswordException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(INCORRECT_CURRENT_PASSWORD.getCode())
                                .errorDescription(INCORRECT_CURRENT_PASSWORD.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<ExceptionResponse> handleException(PasswordNotMatchException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(NEW_PASSWORD_DOES_NOT_MATCH.getCode())
                                .errorDescription(NEW_PASSWORD_DOES_NOT_MATCH.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ExceptionResponse> handleException(FileSizeLimitExceededException exception) {
        return ResponseEntity
                .status(PAYLOAD_TOO_LARGE)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidFileTypeException exception) {
        return ResponseEntity
                .status(UNSUPPORTED_MEDIA_TYPE)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }

}
