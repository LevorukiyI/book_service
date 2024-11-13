package com.modsensoftware.book_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsensoftware.book_service.config.ApplicationConfig;
import com.modsensoftware.book_service.config.security.SecurityConfiguration;
import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.repositories.BookRepository;
import com.modsensoftware.book_service.requests.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfiguration.class, ApplicationConfig.class})
@ActiveProfiles("test")
public class BooksControllerTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        tearDown();
    }

    @AfterEach
    public void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/books/get-all-books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    public void testGetBookById() throws Exception {
        // Добавление тестовой книги
        BookEntity book = new BookEntity(null, "978-3-16-148410-0", "Test Book", "Fiction", "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(get("/books/get-book/id/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @WithMockUser
    public void testGetBookByIsbn() throws Exception {
        // Добавление тестовой книги
        BookEntity book = new BookEntity(null, "978-3-16-148410-0", "Test Book", "Fiction", "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(get("/books/get-book/isbn/{isbn}", book.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @WithMockUser
    public void testAddBook() throws Exception {
        AddBookRequest addBookRequest = new AddBookRequest("978-3-16-148410-0", "New Book", "Fiction", "New description", "New Author");

        mockMvc.perform(post("/books/add-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addBookRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testEditBook() throws Exception {
        // Добавление тестовой книги
        BookEntity book = BookEntity.builder()
                .isbn("978-3-16-148410-0")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();
        bookRepository.save(book);

        EditBookRequest editBookRequest = new EditBookRequest(book.getId(), "978-3-16-148410-0", "Updated Book", null, null, null);

        mockMvc.perform(post("/books/edit-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editBookRequest)))
                .andExpect(status().isOk());

        // Проверка обновления
        mockMvc.perform(get("/books/get-book/id/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    @WithMockUser
    public void testDeleteBookById() throws Exception {
        // Добавление тестовой книги
        BookEntity book = new BookEntity(null, "978-3-16-148410-0", "Test Book", "Fiction", "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(delete("/books/delete/id/{id}", book.getId()))
                .andExpect(status().isNoContent());

        // Проверка, что книга была удалена
        mockMvc.perform(get("/books/get-book/id/{id}", book.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDeleteBookByIsbn() throws Exception {
        // Добавление тестовой книги
        BookEntity book = new BookEntity(null, "978-3-16-148410-0", "Test Book", "Fiction", "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(delete("/books/delete/isbn/{isbn}", book.getIsbn()))
                .andExpect(status().isNoContent());

        // Проверка, что книга была удалена
        mockMvc.perform(get("/books/get-book/isbn/{isbn}", book.getIsbn()))
                .andExpect(status().isNotFound());
    }

}