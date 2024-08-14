package com.example.assessment.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LibraryError {
    PATRON_NOT_FOUND(HttpStatus.NOT_FOUND, "Patron is not found");

    private final HttpStatus status;
    private final String message;

    LibraryError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
