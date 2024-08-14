package com.example.assessment.controllers;

import com.example.assessment.constants.Url;
import com.example.assessment.dtos.BookDTO;
import com.example.assessment.dtos.ResponseDTO;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(Url.BOOKS)
public class BooksController {

    @Autowired
    private BooksService booksService;

    @PostMapping("")
    public Mono<ResponseDTO<BookDTO>> addBook(@Validated @RequestBody BookDTO book) {
        return booksService.addBook(book)
                .map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, "Book is added successfully"));
    }

    @GetMapping
    public Mono<ResponseDTO<List<BookDTO>>> getBooks() {
        return booksService.getBooks().map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDTO<BookDTO>> getBookById(@PathVariable Long id) {
        return booksService.getBookById(id).map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null))
                .onErrorResume(LibraryException.class,
                        ex -> Mono.just(new ResponseDTO<>(ex.getError().getStatus().value(), null, ex.getMessage())));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDTO<BookDTO>> getBookById(@PathVariable Long id, @Validated @RequestBody BookDTO book) {
        return booksService.updateBookById(id, book).map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null)).onErrorResume(LibraryException.class,
                ex -> Mono.just(new ResponseDTO<>(ex.getError().getStatus().value(), null, ex.getMessage())));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseDTO<Boolean>> deleteBookById(@PathVariable Long id) {
        return booksService.deleteBookById(id).map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null));
    }

}
