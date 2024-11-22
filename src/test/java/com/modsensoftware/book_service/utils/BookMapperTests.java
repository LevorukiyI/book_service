package com.modsensoftware.book_service.utils;

import com.modsensoftware.book_service.dtos.requests.BookRequest;
import com.modsensoftware.book_service.models.BookEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookMapperTests {
    @Test
    public void testFrom_ShouldMapAddBookRequestToBookEntity() {
        BookRequest addBookRequest = new BookRequest(
                "1234567890",
                "Test Book",
                "Fiction",
                "A book for testing",
                "John Doe"
        );

        BookEntity bookEntity = BookMapper.INSTANCE.toEntity(addBookRequest);

        assertEquals(addBookRequest.isbn(), bookEntity.getIsbn());
        assertEquals(addBookRequest.title(), bookEntity.getTitle());
        assertEquals(addBookRequest.genre(), bookEntity.getGenre());
        assertEquals(addBookRequest.description(), bookEntity.getDescription());
        assertEquals(addBookRequest.author(), bookEntity.getAuthor());
    }

    @Test
    public void testFrom_ShouldMapAllFields() {
        BookRequest addBookRequest = new BookRequest(
                "9876543210",
                "Another Test Book",
                "Non-Fiction",
                "Another book for testing",
                "Jane Doe"
        );

        BookEntity bookEntity = BookMapper.INSTANCE.toEntity(addBookRequest);

        assertEquals("9876543210", bookEntity.getIsbn());
        assertEquals("Another Test Book", bookEntity.getTitle());
        assertEquals("Non-Fiction", bookEntity.getGenre());
        assertEquals("Another book for testing", bookEntity.getDescription());
        assertEquals("Jane Doe", bookEntity.getAuthor());
    }

    @Test
    public void testHttpRequestUtilsConstructor() {
        BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
        assertNotNull(bookMapper);
    }
}
