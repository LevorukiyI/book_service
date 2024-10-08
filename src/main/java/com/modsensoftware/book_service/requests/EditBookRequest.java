package com.modsensoftware.book_service.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditBookRequest {
    @NonNull
    private Long id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
}
