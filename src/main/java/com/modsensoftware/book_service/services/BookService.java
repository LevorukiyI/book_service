package com.modsensoftware.book_service.services;

import com.modsensoftware.book_service.dtos.BookDTO;
import com.modsensoftware.book_service.dtos.requests.BookRequest;
import com.modsensoftware.book_service.exceptions.BookAlreadyExistsException;
import com.modsensoftware.book_service.exceptions.BookNotFoundException;
import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.repositories.BookRepository;
import com.modsensoftware.book_service.utils.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookDTO getBookDTO(Long id){
        BookEntity bookEntity = this.getBook(id);
        return BookMapper.INSTANCE.toDTO(bookEntity);
    }

    public BookDTO getBookDTO(String isbn){
        BookEntity bookEntity = this.getBook(isbn);
        return BookMapper.INSTANCE.toDTO(bookEntity);
    }

    public List<BookDTO> getAllBooksAsDTO(){
        List<BookEntity> bookEntities = bookRepository.findAll();
        return bookEntities.stream()
                .map(BookMapper.INSTANCE::toDTO)
                .toList();
    }

    @Transactional
    public BookDTO addBook(BookRequest addBookRequest){
        if(bookRepository.existsByIsbn(addBookRequest.isbn())){
            throw new BookAlreadyExistsException(addBookRequest.isbn());
        }
        BookEntity bookEntity = BookMapper.INSTANCE.toEntity(addBookRequest);
        bookRepository.save(bookEntity);
        return BookMapper.INSTANCE.toDTO(bookEntity);
    }

    @Transactional
    public BookDTO editBook(Long bookId, BookRequest editBookRequest){
        if(!bookRepository.existsById(bookId)){
            throw new BookNotFoundException(bookId);
        }
        if(bookRepository.existsByIsbn(editBookRequest.isbn())){
            throw new BookAlreadyExistsException(editBookRequest.isbn());
        }
        BookEntity bookEntity = BookMapper.INSTANCE.toEntity(bookId, editBookRequest);

        bookRepository.save(bookEntity);
        return BookMapper.INSTANCE.toDTO(bookEntity);
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

    private BookEntity getBook(Long id){
        return bookRepository.getBookEntityById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    private BookEntity getBook(String isbn){
        return bookRepository.getBookEntityByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }
}
