package com.example.assessment.repositories;

import com.example.assessment.domains.Books;
import com.example.assessment.domains.Patrons;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PatronRepository extends R2dbcRepository<Patrons, Long>  {
}