package com.modsensoftware.book_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookAlreadyExistsException extends IllegalArgumentException {
    public BookAlreadyExistsException(String isbn) {
        super("Books with ISBN - " + isbn + " already exists. ISBN must be unique.");
    }
}
