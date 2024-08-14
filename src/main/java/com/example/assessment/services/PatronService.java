package com.example.assessment.services;

import com.example.assessment.dtos.PatronDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PatronService {

    Mono<List<PatronDTO>> getPatrons();

    Mono<PatronDTO> getPatronById(Long id);

    Mono<PatronDTO> addPatron(PatronDTO patron);

    Mono<PatronDTO> updatePatronById(Long id, PatronDTO patron);

    Mono<Boolean> deletePatronById(Long id);

}
