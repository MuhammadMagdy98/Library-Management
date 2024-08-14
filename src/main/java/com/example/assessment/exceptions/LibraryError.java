package com.example.assessment.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LibraryError {
    PATRON_NOT_FOUND(HttpStatus.NOT_FOUND, "Patron is not found"),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "Book is not found"),
    BOOK_NOT_AVAILABLE(HttpStatus.CONFLICT, "Book is not available"),
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "Record is not found");

    private final HttpStatus status;
    private final String message;

    LibraryError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
