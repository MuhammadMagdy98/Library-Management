package com.example.assessment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;


@ToString
@Data
public class BookDTO implements Serializable {

    private Long id;
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;
    @NotNull(message = "Publication year cannot be null")
    @PastOrPresent(message = "Publication year must be in the past or present")
    private Instant publicationYear;
    @NotBlank(message = "ISBN cannot be blank")
    private String ISBN;
}
