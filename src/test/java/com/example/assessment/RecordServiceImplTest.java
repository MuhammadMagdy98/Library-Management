package com.example.assessment;

import com.example.assessment.domains.Book;
import com.example.assessment.domains.Record;
import com.example.assessment.domains.Patron;
import com.example.assessment.exceptions.LibraryError;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.repositories.BookRepository;
import com.example.assessment.repositories.PatronRepository;
import com.example.assessment.repositories.RecordRepository;
import com.example.assessment.services.RecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecordServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordServiceImpl recordService;

    private Book book;
    private Patron patron;
    private Record record;

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

        patron = new Patron();
        patron.setId(1L);
        patron.setName("Patron Name");
        patron.setContactInfo("patron@example.com");

        record = new Record();
        record.setId(1L);
        record.setBookId(book.getId());
        record.setPatronId(patron.getId());
        record.setBorrowDate(Instant.now());
    }

    @Test
    void testBorrowBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
        when(patronRepository.findById(1L)).thenReturn(Mono.just(patron));
        when(recordRepository.save(any(Record.class))).thenReturn(Mono.just(record));

        Mono<Boolean> result = recordService.borrowBook(1L, 1L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
        verify(patronRepository, times(1)).findById(1L);
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Boolean> result = recordService.borrowBook(1L, 1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.BOOK_NOT_FOUND)
                .verify();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
        verify(patronRepository, never()).findById(anyLong());
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    void testBorrowBook_PatronNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
        when(patronRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Boolean> result = recordService.borrowBook(1L, 1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.PATRON_NOT_FOUND)
                .verify();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
        verify(patronRepository, times(1)).findById(1L);
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        book.setIsBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Mono.just(book));

        Mono<Boolean> result = recordService.borrowBook(1L, 1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.BOOK_NOT_AVAILABLE)
                .verify();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
        verify(patronRepository, never()).findById(anyLong());
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    void testReturnBook_Success() {
        when(recordRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Mono.just(record));
        when(recordRepository.save(any(Record.class))).thenReturn(Mono.just(record));
        when(bookRepository.findById(1L)).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        Mono<Boolean> result = recordService.returnBook(1L, 1L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(recordRepository, times(1)).findByBookIdAndPatronId(1L, 1L);
        verify(recordRepository, times(1)).save(record);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testReturnBook_RecordNotFound() {
        when(recordRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Mono.empty());

        Mono<Boolean> result = recordService.returnBook(1L, 1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.RECORD_NOT_FOUND)
                .verify();

        verify(recordRepository, times(1)).findByBookIdAndPatronId(1L, 1L);
        verify(recordRepository, never()).save(any(Record.class));
        verify(bookRepository, never()).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }
}