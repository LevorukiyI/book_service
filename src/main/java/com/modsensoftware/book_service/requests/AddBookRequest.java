package com.modsensoftware.book_service.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddBookRequest {
    @NonNull
    private String isbn;

    @NonNull
    private String title;

    private String genre;
    private String description;

    @NonNull
    private String author;
}
