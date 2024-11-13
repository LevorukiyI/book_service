package com.modsensoftware.book_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("book with ID " + id + " not found");
    }

    public BookNotFoundException(String isbn) {
        super("book with ISBN " + isbn + " not found");
    }
}
