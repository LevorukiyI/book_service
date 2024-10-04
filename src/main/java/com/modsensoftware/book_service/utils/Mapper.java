package com.modsensoftware.book_service.utils;

import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.requests.AddBookRequest;

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
}
