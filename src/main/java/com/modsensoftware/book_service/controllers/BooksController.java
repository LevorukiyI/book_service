package com.modsensoftware.book_service.controllers;

import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.requests.AddBookRequest;
import com.modsensoftware.book_service.requests.EditBookRequest;
import com.modsensoftware.book_service.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @Operation(summary = "Получение списка всех книг",
            security = @SecurityRequirement(name = "Bearer"))
    @GetMapping("/get-all-books")
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        List<BookEntity> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Получение книги по ID",
            parameters = @Parameter(name = "id", description = "ID книги", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @GetMapping("/get-book/id/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Long id) {
        BookEntity book = bookService.getBook(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Получение книги по ISBN",
            parameters = @Parameter(name = "isbn", description = "ISBN книги", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @GetMapping("/get-book/isbn/{isbn}")
    public ResponseEntity<BookEntity> getBookByIsbn(@PathVariable String isbn) {
        BookEntity book = bookService.getBook(isbn);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Добавление новой книги",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Информация о книге", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @PostMapping("/add-book")//TODO redundant link
    public ResponseEntity<Void> addBook(@RequestBody AddBookRequest addBookRequest) {
        bookService.addBook(addBookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Изменение информации о существующей книге",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Обновленная информация о книге", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @PostMapping("/edit-book")
    public ResponseEntity<Void> editBook(@RequestBody EditBookRequest editBookRequest) {
        bookService.editBook(editBookRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление книги по ID",
            parameters = @Parameter(name = "id", description = "ID книги", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @DeleteMapping("/delete/id/{id}")//TODO redundant link
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удаление книги по ISBN",
            parameters = @Parameter(name = "isbn", description = "ISBN книги", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @DeleteMapping("/delete/isbn/{isbn}")
    public ResponseEntity<Void> deleteBookByIsbn(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }
}
