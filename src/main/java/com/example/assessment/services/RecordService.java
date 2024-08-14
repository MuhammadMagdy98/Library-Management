package com.example.assessment.services;

import reactor.core.publisher.Mono;

public interface RecordService {

    Mono<Boolean> borrowBook(Long bookId, Long patronId);

    Mono<Boolean> returnBook(Long bookId, Long patronId);
}
