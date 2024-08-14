package com.example.assessment.domains;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Column(name = "publication_year")
    private Instant publicationYear;

    @Column(name = "isbn")
    private String ISBN;

    @Column(name="is_borrowed")
    private Boolean isBorrowed;
}
