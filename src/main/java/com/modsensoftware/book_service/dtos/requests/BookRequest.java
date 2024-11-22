package com.modsensoftware.book_service.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "BookRequest, contains necessary details to create or edit book.")
public record BookRequest (
        @NotBlank(message = "isbn can't be blank")
        @Schema(description = "The ISBN of the book, which must be a non-blank string.", example = "978-3-16-148410-0")
        String isbn,

        @NotBlank(message = "title of book can't be blank")
        @Schema(description = "The title of the book, which must be a non-blank string.", example = "Effective Java")
        String title,

        @Schema(description = "The genre of the book.", example = "Programming")
        String genre,

        @Schema(description = "A brief description of the book.", example = "This book provides best practices for programming in Java.")
        String description,

        @NotBlank(message = "author field can't be blank")
        @Schema(description = "The author of the book, which must be a non-blank string.", example = "Joshua Bloch")
        String author
) {
}