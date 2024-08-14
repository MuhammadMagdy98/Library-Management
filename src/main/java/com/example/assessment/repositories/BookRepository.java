package com.example.assessment.repositories;

import com.example.assessment.domains.Books;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookRepository extends R2dbcRepository<Books, Long> {
}
