package com.modsensoftware.book_service.models;

import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "author", nullable = false)
    private String author;
}
