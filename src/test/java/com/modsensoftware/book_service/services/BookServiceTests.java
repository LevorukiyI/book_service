package com.modsensoftware.book_service.services;

import com.modsensoftware.book_service.exceptions.BookNotFoundException;
import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.repositories.BookRepository;
import com.modsensoftware.book_service.requests.AddBookRequest;
import com.modsensoftware.book_service.requests.EditBookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private BookEntity bookEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setIsbn("123-4567890123");
        bookEntity.setTitle("Test Book");
    }

    @Test
    void getAllBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(bookEntity));

        var books = bookService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals(bookEntity, books.get(0));
    }

    @Test
    void getBookById() {
        when(bookRepository.getBookEntityById(1L)).thenReturn(Optional.of(bookEntity));

        var result = bookService.getBook(1L);

        assertEquals(bookEntity, result);
    }

    @Test
    void getBookById_NotFound() {
        when(bookRepository.getBookEntityById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBook(1L));
    }

    @Test
    void addBook() {
        AddBookRequest request = new AddBookRequest();
        request.setIsbn("123-4567890123");
        request.setTitle("Test Book");

        bookService.addBook(request);

        ArgumentCaptor<BookEntity> captor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository).save(captor.capture());

        BookEntity savedBook = captor.getValue();
        assertEquals("123-4567890123", savedBook.getIsbn());
        assertEquals("Test Book", savedBook.getTitle());
    }

    @Test
    void editBook() {
        EditBookRequest request = new EditBookRequest();
        request.setId(1L);
        request.setTitle("Updated Book");

        when(bookRepository.getBookEntityById(1L)).thenReturn(Optional.of(bookEntity));

        bookService.editBook(request);

        verify(bookRepository).save(bookEntity);
        assertEquals("Updated Book", bookEntity.getTitle());
    }

    @Test
    void deleteBookById() {
        when(bookRepository.getBookEntityById(1L)).thenReturn(Optional.of(bookEntity));

        bookService.deleteBook(1L);

        verify(bookRepository).delete(bookEntity);
    }

    @Test
    void deleteBookById_NotFound() {
        when(bookRepository.getBookEntityById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }

    @Test
    void deleteBookByIsbn() {
        when(bookRepository.getBookEntityByIsbn("123-4567890123")).thenReturn(Optional.of(bookEntity));

        bookService.deleteBook("123-4567890123");

        verify(bookRepository).delete(bookEntity);
    }

    @Test
    void getBookByIsbn_NotFound() {
        when(bookRepository.getBookEntityByIsbn("123-4567890123")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBook("123-4567890123"));
    }
}
