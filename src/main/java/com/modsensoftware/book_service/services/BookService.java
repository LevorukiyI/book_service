package com.modsensoftware.book_service.services;

import com.modsensoftware.book_service.exceptions.BookNotFoundException;
import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.repositories.BookRepository;
import com.modsensoftware.book_service.requests.AddBookRequest;
import com.modsensoftware.book_service.requests.EditBookRequest;
import com.modsensoftware.book_service.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookEntity> getAllBooks(){
        return bookRepository.findAll();
    }

    public BookEntity getBook(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID книги не может быть null");
        }
        return bookRepository.getBookEntityById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public BookEntity getBook(String isbn){
        if (isbn == null) {
            throw new IllegalArgumentException("isbn книги не может быть null");
        }
        return bookRepository.getBookEntityByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Transactional
    public BookEntity addBook(AddBookRequest addBookRequest){
        BookEntity bookEntity = Mapper.from(addBookRequest);
        bookRepository.save(bookEntity);
        return bookEntity;
    }

    @Transactional
    public BookEntity editBook(Long bookId, EditBookRequest editBookRequest){
        if (bookId == null) {
            throw new IllegalArgumentException("ID книги не может быть null");
        }
        if(!bookRepository.existsById(bookId)){
            throw new BookNotFoundException(bookId);
        }
        BookEntity bookEntity = Mapper.from(bookId, editBookRequest);

        bookRepository.save(bookEntity);
        return bookEntity;
    }

    @Transactional
    public void deleteBook(Long id){
        BookEntity bookEntity = getBook(id);
        bookRepository.delete(bookEntity);
    }

    @Transactional
    public void deleteBook(final String isbn){
        BookEntity bookEntity = getBook(isbn);
        bookRepository.delete(bookEntity);
    }
}
