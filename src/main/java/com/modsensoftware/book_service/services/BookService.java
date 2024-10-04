package com.modsensoftware.book_service.services;

import com.modsensoftware.book_service.exceptions.BookNotFoundException;
import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.repositories.BookRepository;
import com.modsensoftware.book_service.requests.AddBookRequest;
import com.modsensoftware.book_service.requests.EditBookRequest;
import com.modsensoftware.book_service.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void addBook(AddBookRequest addBookRequest){
        BookEntity bookEntity = Mapper.from(addBookRequest);
        bookRepository.save(bookEntity);
    }

    public void editBook(EditBookRequest editBookRequest){
        BookEntity bookEntity = getBook(editBookRequest.getId());

        if (editBookRequest.getIsbn() != null) {
            bookEntity.setIsbn(editBookRequest.getIsbn());
        }
        if (editBookRequest.getTitle() != null) {
            bookEntity.setTitle(editBookRequest.getTitle());
        }
        if (editBookRequest.getGenre() != null) {
            bookEntity.setGenre(editBookRequest.getGenre());
        }
        if (editBookRequest.getDescription() != null) {
            bookEntity.setDescription(editBookRequest.getDescription());
        }
        if (editBookRequest.getAuthor() != null) {
            bookEntity.setAuthor(editBookRequest.getAuthor());
        }

        bookRepository.save(bookEntity);
    }

    public void deleteBook(Long id){
        BookEntity bookEntity = getBook(id);
        bookRepository.delete(bookEntity);
    }

    public void deleteBook(String isbn){
        BookEntity bookEntity = getBook(isbn);
        bookRepository.delete(bookEntity);
    }
}
