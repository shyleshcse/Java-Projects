package com.example.library.exception;

// Thrown when the same book name + author already exists. Handled in GlobalExceptionHandler (409).
public class DuplicateBookException extends RuntimeException {

    public DuplicateBookException(String message) {
        super(message);
    }
}
