package com.modsensoftware.book_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsensoftware.book_service.annotations.ContainerTest;
import com.modsensoftware.book_service.config.ApplicationConfig;
import com.modsensoftware.book_service.config.security.SecurityConfiguration;
import com.modsensoftware.book_service.controllers.BooksController;
import com.modsensoftware.book_service.dtos.requests.BookRequest;
import com.modsensoftware.book_service.models.BookEntity;
import com.modsensoftware.book_service.repositories.BookRepository;
import com.modsensoftware.book_service.security.filters.ApiKeyAuthenticationFilter;
import com.modsensoftware.book_service.services.BookService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfiguration.class, ApplicationConfig.class})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContainerTest
public class BooksControllerTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    private MockMvc mockMvc;

    @Autowired
    private ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;

    private ObjectMapper objectMapper;

    @Value("${application.security.this-service-secret-api-key}")
    private String thisServiceSecretApiKey;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BooksController(bookService))
                .addFilters(apiKeyAuthenticationFilter)
                .build();
        tearDown();
    }

    public void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/books")
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetBookById() throws Exception {
        BookEntity book = new BookEntity(null, "978-3-16-148410-0", "Test Book", "Fiction",
                "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(get("/books/{id}", book.getId())
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    public void testEditBook() throws Exception {
        BookEntity book = BookEntity.builder()
                .isbn("978-3-16-148410-0")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();
        bookRepository.save(book);

        BookRequest editBookRequest = new BookRequest("978-3-16-148410-0",
                "Updated Book", null, null, "new Author");

        mockMvc.perform(put("/books/{id}", book.getId())
                        .header("x-api-key", thisServiceSecretApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editBookRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books/{id}", book.getId())
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    public void testGetBookByIsbn() throws Exception {
        BookEntity book = new BookEntity(null, "978-3-16-148410-0", "Test Book", "Fiction",
                "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(get("/books/isbn/{isbn}", book.getIsbn())
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    public void testAddBook() throws Exception {
        BookRequest addBookRequest = new BookRequest("978-3-16-148410-0",
                "New Book", "Fiction", "New description", "New Author");

        mockMvc.perform(post("/books")
                        .header("x-api-key", thisServiceSecretApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addBookRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddBooksWithSameISBN() throws Exception {
        BookRequest addBookRequest1 = new BookRequest("978-3-16-148410-0",
                "New Book", "Fiction", "New description", "New Author");

        mockMvc.perform(post("/books")
                        .header("x-api-key", thisServiceSecretApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addBookRequest1)))
                .andExpect(status().isCreated());

        BookRequest addBookRequest2 = new BookRequest("978-3-16-148410-0",
                "New Book2", "NonFiction", "New description2", "Another author");

        mockMvc.perform(post("/books")
                        .header("x-api-key", thisServiceSecretApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addBookRequest2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteBookById() throws Exception {
        BookEntity book = new BookEntity(null, "978-3-16-148410-0",
                "Test Book", "Fiction", "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(delete("/books/{id}", book.getId())
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books/{id}", book.getId())
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBookByIsbn() throws Exception {
        BookEntity book = new BookEntity(null, "978-3-16-148410-0", "Test Book",
                "Fiction", "Test description", "Test Author");
        bookRepository.save(book);

        mockMvc.perform(delete("/books/isbn/{isbn}", book.getIsbn())
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books/isbn/{isbn}", book.getIsbn())
                        .header("x-api-key", thisServiceSecretApiKey))
                .andExpect(status().isNotFound());
    }

}