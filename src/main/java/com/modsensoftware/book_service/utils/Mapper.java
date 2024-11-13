package com.modsensoftware.book_service.utils;

import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.requests.AddBookRequest;
import com.modsensoftware.book_service.requests.EditBookRequest;

public class Mapper {
    public static BookEntity from(AddBookRequest addBookRequest){
        return BookEntity.builder()
                .isbn(addBookRequest.getIsbn())
                .title(addBookRequest.getTitle())
                .genre(addBookRequest.getGenre())
                .description(addBookRequest.getDescription())
                .author(addBookRequest.getAuthor())
                .build();
    }

    public static BookEntity from(Long bookId, EditBookRequest editBookRequest){
        return BookEntity.builder()
                .id(bookId)
                .isbn(editBookRequest.getIsbn())
                .title(editBookRequest.getTitle())
                .genre(editBookRequest.getGenre())
                .description(editBookRequest.getDescription())
                .author(editBookRequest.getAuthor())
                .build();
    }
}
