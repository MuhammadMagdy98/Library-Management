package com.example.assessment.services;

import com.example.assessment.domains.Book;
import com.example.assessment.dtos.BookDTO;
import com.example.assessment.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BookRepository bookRepository;


    private BookDTO getBookDTO(Book book) {
        BookDTO savedBookDTO = new BookDTO();
        savedBookDTO.setId(book.getId());
        savedBookDTO.setTitle(book.getTitle());
        savedBookDTO.setAuthor(book.getAuthor());
        savedBookDTO.setISBN(book.getISBN());
        savedBookDTO.setPublicationYear(book.getPublicationYear());
        return savedBookDTO;

    }

    @Override
    public Mono<BookDTO> addBook(BookDTO book) {
        return Mono.fromSupplier(() -> {
                    Book myBook = new Book();
                    myBook.setAuthor(book.getAuthor());
                    myBook.setTitle(book.getTitle());
                    myBook.setISBN(book.getISBN());
                    myBook.setPublicationYear(book.getPublicationYear());
                    myBook.setIsBorrowed(false);
                    return myBook;
                })
                .flatMap(bookRepository::save)
                .doOnNext(savedBook -> {
                    System.out.println("Saved Book ID: " + savedBook.toString());
                })
                .map(this::getBookDTO);
    }

    @Override
    public Mono<List<BookDTO>> getBooks() {
        return bookRepository.findAll()
                .map(this::getBookDTO)
                .collectList();
    }

    @Override
    public Mono<BookDTO> getBookById(Long id) {
        return bookRepository.findById(id).map(this::getBookDTO);
    }

    @Override
    public Mono<BookDTO> updateBookById(Long id, BookDTO book) {
        return bookRepository.findById(id).flatMap(books -> {
            books.setAuthor(book.getAuthor());
            books.setISBN(book.getISBN());
            books.setTitle(book.getTitle());
            books.setPublicationYear(book.getPublicationYear());
            return bookRepository.save(books);
        }).map(this::getBookDTO);
    }
    @Override
    public Mono<Boolean> deleteBookById(Long id) {
        return bookRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return bookRepository.deleteById(id)
                                .then(Mono.just(true));
                    } else {
                        return Mono.just(false);
                    }
                });
    }

}
