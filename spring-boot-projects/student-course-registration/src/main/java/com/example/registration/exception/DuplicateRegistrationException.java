package com.example.registration.exception;

// Thrown when the same email is already registered. Handled in GlobalExceptionHandler (409).
public class DuplicateRegistrationException extends RuntimeException {

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
