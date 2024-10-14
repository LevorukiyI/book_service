package com.modsensoftware.book_service.utils;

import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.requests.AddBookRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapperTests {

    @Test
    public void testFrom_ShouldMapAddBookRequestToBookEntity() {
        AddBookRequest addBookRequest = AddBookRequest.builder()
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("A book for testing")
                .author("John Doe")
                .build();

        BookEntity bookEntity = Mapper.from(addBookRequest);

        assertEquals(addBookRequest.getIsbn(), bookEntity.getIsbn());
        assertEquals(addBookRequest.getTitle(), bookEntity.getTitle());
        assertEquals(addBookRequest.getGenre(), bookEntity.getGenre());
        assertEquals(addBookRequest.getDescription(), bookEntity.getDescription());
        assertEquals(addBookRequest.getAuthor(), bookEntity.getAuthor());
    }

    @Test
    public void testFrom_ShouldMapAllFields() {
        AddBookRequest addBookRequest = AddBookRequest.builder()
                .isbn("9876543210")
                .title("Another Test Book")
                .genre("Non-Fiction")
                .description("Another book for testing")
                .author("Jane Doe")
                .build();

        BookEntity bookEntity = Mapper.from(addBookRequest);

        assertEquals("9876543210", bookEntity.getIsbn());
        assertEquals("Another Test Book", bookEntity.getTitle());
        assertEquals("Non-Fiction", bookEntity.getGenre());
        assertEquals("Another book for testing", bookEntity.getDescription());
        assertEquals("Jane Doe", bookEntity.getAuthor());
    }

    @Test
    public void testHttpRequestUtilsConstructor() {
        Mapper mapper = new Mapper();
        assertNotNull(mapper);
    }
}
