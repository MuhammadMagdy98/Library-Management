package com.example.assessment.controllers;

import com.example.assessment.dtos.PatronDTO;
import com.example.assessment.dtos.ResponseDTO;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.services.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.example.assessment.constants.Url;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(Url.PATRON)
public class PatronController {
    @Autowired
    private PatronService patronService;

    @PostMapping("")
    public Mono<ResponseDTO<PatronDTO>> addPatron(@Validated @RequestBody PatronDTO patron) {
        return patronService.addPatron(patron)
                .map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, "Patron is added successfully"));
    }

    @GetMapping
    public Mono<ResponseDTO<List<PatronDTO>>> getPatrons() {
        return patronService.getPatrons().map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDTO<PatronDTO>> getBookById(@PathVariable Long id) {
        return patronService.getPatronById(id).map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null))
                .onErrorResume(LibraryException.class,
                        ex -> Mono.just(new ResponseDTO<>(ex.getError().getStatus().value(), null, ex.getMessage())));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDTO<PatronDTO>> updatePatronById(@PathVariable Long id,
            @Validated @RequestBody PatronDTO patron) {
        return patronService.updatePatronById(id, patron)
                .map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseDTO<Boolean>> deletePatronById(@PathVariable Long id) {
        return patronService.deletePatronById(id).map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null));
    }

}
