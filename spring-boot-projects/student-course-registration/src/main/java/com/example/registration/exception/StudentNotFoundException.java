package com.example.registration.exception;

// Thrown when no student exists for the given ID/email. Handled in GlobalExceptionHandler (404).
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }
}
