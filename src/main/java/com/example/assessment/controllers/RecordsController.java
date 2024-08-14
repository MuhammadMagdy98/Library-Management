package com.example.assessment.controllers;

import com.example.assessment.dtos.BookDTO;
import com.example.assessment.dtos.ResponseDTO;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.assessment.constants.Url;
import reactor.core.publisher.Mono;


@RestController
public class RecordsController {
    @Autowired
    private RecordService recordService;

    @PostMapping(Url.BORROW + "/{bookId}/patron/{patronId}")
    public Mono<ResponseDTO<Boolean>> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return recordService.borrowBook(bookId, patronId).map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, "Book is borrowed successfully")).onErrorResume(LibraryException.class, ex ->
                Mono.just(new ResponseDTO<>(ex.getError().getStatus().value(), null, ex.getMessage())));
    }

    @PutMapping(Url.RETURN + "/{bookId}/patron/{patronId}")
    public Mono<ResponseDTO<Boolean>> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return recordService.returnBook(bookId, patronId).map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, "Book is returned successfully")).onErrorResume(LibraryException.class, ex ->
                Mono.just(new ResponseDTO<>(ex.getError().getStatus().value(), null, ex.getMessage())));
    }

}
