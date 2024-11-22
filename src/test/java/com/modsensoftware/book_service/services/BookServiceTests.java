package com.modsensoftware.book_service.services;

import com.modsensoftware.book_service.dtos.BookDTO;
import com.modsensoftware.book_service.dtos.requests.BookRequest;
import com.modsensoftware.book_service.exceptions.BookNotFoundException;
import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.repositories.BookRepository;
import com.modsensoftware.book_service.utils.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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

        var books = bookService.getAllBooksAsDTO();

        assertEquals(1, books.size());
        assertEquals(BookMapper.INSTANCE.toDTO(bookEntity), books.get(0));
    }

    @Test
    void getBookById() {
        when(bookRepository.getBookEntityById(1L)).thenReturn(Optional.of(bookEntity));

        var result = bookService.getBookDTO(1L);

        assertEquals(BookMapper.INSTANCE.toDTO(bookEntity), result);
    }

    @Test
    void getBookById_NotFound() {
        when(bookRepository.getBookEntityById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookDTO(1L));
    }

    @Test
    void addBook() {
        BookRequest request = new BookRequest("123-4567890123", "Test Book", null,
                null, "author");

        bookService.addBook(request);

        ArgumentCaptor<BookEntity> captor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository).save(captor.capture());

        BookEntity savedBook = captor.getValue();
        assertEquals("123-4567890123", savedBook.getIsbn());
        assertEquals("Test Book", savedBook.getTitle());
    }

    @Test
    void editBook() {
        BookRequest request = new BookRequest("isbn", "Updated Book", null, null, "author");
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.getBookEntityById(bookId)).thenReturn(Optional.of(bookEntity));

        BookDTO resultBook = bookService.editBook(bookId, request);

        verify(bookRepository).save(any());
        assertEquals("Updated Book", resultBook.title());
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

        assertThrows(BookNotFoundException.class, () -> bookService.getBookDTO("123-4567890123"));
    }
}
