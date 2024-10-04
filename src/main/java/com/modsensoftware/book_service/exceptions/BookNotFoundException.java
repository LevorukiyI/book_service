package com.modsensoftware.book_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Книга с ID " + id + " не найдена");
    }

    public BookNotFoundException(String isbn) {
        super("Книга с ISBN " + isbn + " не найдена");
    }
}
