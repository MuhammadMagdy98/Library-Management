package com.example.assessment.repositories;

import com.example.assessment.domains.Record;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RecordRepository extends R2dbcRepository<Record, Long> {

    @Query("SELECT * FROM borrowing_records WHERE book_id = :bookId AND patron_id = :patronId AND return_date IS NULL")
    Mono<Record> findByBookIdAndPatronId(Long bookId, Long patronId);
}
