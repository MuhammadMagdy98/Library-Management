package com.example.assessment;

import com.example.assessment.domains.Book;
import com.example.assessment.dtos.BookDTO;
import com.example.assessment.exceptions.LibraryError;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.repositories.BookRepository;
import com.example.assessment.services.BooksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BooksServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BooksServiceImpl booksService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setISBN("123456789");
        book.setPublicationYear(Instant.now());
        book.setIsBorrowed(false);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setAuthor("Author");
        bookDTO.setTitle("Title");
        bookDTO.setISBN("123456789");
        bookDTO.setPublicationYear(Instant.now());
        bookDTO.setIsBorrowed(false);
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        Mono<BookDTO> result = booksService.addBook(bookDTO);

        StepVerifier.create(result)
                .expectNextMatches(bookDTO1 -> bookDTO1.getId().equals(book.getId()))
                .verifyComplete();

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testGetBooks() {
        when(bookRepository.findAll()).thenReturn(Flux.just(book));

        Mono<List<BookDTO>> result = booksService.getBooks();

        StepVerifier.create(result)
                .expectNextMatches(bookDTOList -> bookDTOList.size() == 1 && bookDTOList.get(0).getId().equals(book.getId()))
                .verifyComplete();

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Mono.just(book));

        Mono<BookDTO> result = booksService.getBookById(1L);

        StepVerifier.create(result)
                .expectNextMatches(bookDTO1 -> bookDTO1.getId().equals(book.getId()))
                .verifyComplete();

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<BookDTO> result = booksService.getBookById(1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.BOOK_NOT_FOUND)
                .verify();

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        Mono<BookDTO> result = booksService.updateBookById(1L, bookDTO);

        StepVerifier.create(result)
                .expectNextMatches(bookDTO1 -> bookDTO1.getId().equals(book.getId()))
                .verifyComplete();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<BookDTO> result = booksService.updateBookById(1L, bookDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.BOOK_NOT_FOUND)
                .verify();

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteBookById_Found() {
        when(bookRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(bookRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Boolean> result = booksService.deleteBookById(1L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookById_NotFound() {
        when(bookRepository.existsById(1L)).thenReturn(Mono.just(false));

        Mono<Boolean> result = booksService.deleteBookById(1L);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, never()).deleteById(1L);
    }
}
