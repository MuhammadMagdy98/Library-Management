package com.example.assessment.services;

import com.example.assessment.domains.Record;
import com.example.assessment.exceptions.LibraryError;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.repositories.BookRepository;
import com.example.assessment.repositories.PatronRepository;
import com.example.assessment.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;
    @Override
    public Mono<Boolean> borrowBook(Long bookId, Long patronId) {
        return bookRepository.findById(bookId)
                .flatMap(book -> {
                    System.out.println(book.toString());
                    if (book.getIsBorrowed() == null || !book.getIsBorrowed()) {
                        book.setIsBorrowed(true);

                        return bookRepository.save(book)
                                .then(patronRepository.findById(patronId)
                                        .flatMap(patron -> {
                                            Record record = new Record();
                                            record.setBookId(bookId);
                                            record.setPatronId(patronId);
                                            record.setBorrowDate(Instant.now());

                                            return recordRepository.save(record)
                                                    .then(Mono.just(true));
                                        })
                                        .switchIfEmpty(Mono.error(new LibraryException(LibraryError.PATRON_NOT_FOUND)))
                                );
                    } else {
                        return Mono.error(new LibraryException(LibraryError.BOOK_NOT_AVAILABLE));
                    }
                })
                .switchIfEmpty(Mono.error(new LibraryException(LibraryError.BOOK_NOT_FOUND)));
    }



    @Override
    public Mono<Boolean> returnBook(Long bookId, Long patronId) {
        return recordRepository.findByBookIdAndPatronId(bookId, patronId)
                .flatMap(record -> {
                    record.setReturnDate(Instant.now());

                    return recordRepository.save(record)
                            .then(bookRepository.findById(bookId))
                            .flatMap(book -> {
                                book.setIsBorrowed(false);

                                return bookRepository.save(book)
                                        .then(Mono.just(true));
                            });
                })
                .switchIfEmpty(Mono.error(new LibraryException(LibraryError.RECORD_NOT_FOUND)));
    }
}
