package com.example.assessment.repositories;

import com.example.assessment.domains.Patron;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PatronRepository extends R2dbcRepository<Patron, Long> {
}