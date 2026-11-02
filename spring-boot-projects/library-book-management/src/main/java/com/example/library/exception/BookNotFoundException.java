package com.example.library.exception;

// Thrown when no book exists for the given ID. Handled in GlobalExceptionHandler (404).
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
}
