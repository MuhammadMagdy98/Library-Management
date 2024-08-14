package com.example.assessment.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryException extends RuntimeException {

    private final LibraryError error;

    public LibraryException(LibraryError err) {
        super(err.getMessage());
        this.error = err;

    }

}
