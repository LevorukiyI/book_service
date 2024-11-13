package com.modsensoftware.book_service.repositories;

import com.modsensoftware.book_service.models.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> getBookEntityById(Long id);
    boolean existsById(Long bookId);
    Optional<BookEntity> getBookEntityByIsbn(String isbn);
}
