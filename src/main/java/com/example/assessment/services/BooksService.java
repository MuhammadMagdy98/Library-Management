package com.example.assessment.services;

import com.example.assessment.dtos.BookDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BooksService {

    Mono<BookDTO> addBook(BookDTO book);
    Mono<List<BookDTO>> getBooks();
    Mono<BookDTO> getBookById(Long id);

}
