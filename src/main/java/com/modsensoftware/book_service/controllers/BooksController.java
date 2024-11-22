package com.modsensoftware.book_service.controllers;

import com.modsensoftware.book_service.dtos.BookDTO;
import com.modsensoftware.book_service.dtos.requests.BookRequest;
import com.modsensoftware.book_service.exceptions.responses.ExceptionResponse;
import com.modsensoftware.book_service.services.BookService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @Operation(summary = "get list of all books",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "You get all books **successfully**",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))))
    })
    @PreAuthorize("hasAuthority('PERMISSION_GET_ALL_BOOKS')")
    @GetMapping()
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooksAsDTO();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "get book by ID",
            parameters = @Parameter(name = "id", description = "book ID", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "you get book by id **successfully**",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "book_service client cant find book with your specified book id <br>",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "you provide null bookId.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PreAuthorize("hasAuthority('PERMISSION_GET_BOOK_BY_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(
            @PathVariable @NotNull(message = "bookID can't be null") Long id) {
        BookDTO book = bookService.getBookDTO(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Get book by ISBN",
            parameters = @Parameter(name = "isbn", description = "book ISBN", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "you get book by ISBN **successfully**",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "book_service client cant find book with your specified book ISBN <br>",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "you didn't provide ISBN, or provide null bookId.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PreAuthorize("hasAuthority('PERMISSION_GET_BOOK_BY_ISBN')")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(
            @NotBlank(message = "ISBN can't be blank") @PathVariable String isbn) {
        BookDTO book = bookService.getBookDTO(isbn);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Add new book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "book information. ISBN must be unique", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "book added **successfully**",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "a specific error description will be passed to the ExceptionResponse. <br>"
                            + "you didn't provide book ID, or provide null book ID."
                            + "**OR** isbn is blank"
                            + "**OR** title is blank"
                            + "**OR** author is blank"
                            + "**OR** books with your specified ISBN already exists.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = BookRequest.class))
    )
    @PreAuthorize("hasAuthority('PERMISSION_ADD_BOOK')")
    @PostMapping()
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookRequest addBookRequest) {
        BookDTO addedBook = bookService.addBook(addBookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
    }

    @Operation(summary = "Edit existing book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Renewed information about book", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "book edited **successfully**",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "400", description = "book with provided isbn already exists" +
                    "(and its not the same book, that you try to edit)",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "403",
                    description = "a specific error description will be passed to the ExceptionResponse. <br>"
                            + "you didn't provide book ID, or provide null book ID."
                            + "**OR** isbn is blank"
                            + "**OR** title is blank"
                            + "**OR** author is blank",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PreAuthorize("hasAuthority('PERMISSION_EDIT_BOOK')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> editBook(
            @PathVariable @NotNull(message = "bookID can't be null") Long id,
            @RequestBody @Valid BookRequest editBookRequest) {
        BookDTO editedBook = bookService.editBook(id, editBookRequest);
        return ResponseEntity.ok(editedBook);
    }

    @Operation(summary = "Delete book by ID",
            parameters = @Parameter(name = "id", description = "id of book, that you want to delete", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "you deleted book by ID **successfully**",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "a specific error description will be passed to the ExceptionResponse. <br>"
                            + "book_service client cant find book with your specified book ID <br>",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "you didn't provide book ID, or provide null book ID.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_BOOK_BY_ID')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(
            @PathVariable @NotNull(message = "bookID can't be null") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Delete book by ISBN",
            parameters = @Parameter(name = "isbn",
                    description = "ISBN of book, that you want to delete", required = true),
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "you deleted book by ISBN **successfully**",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "a specific error description will be passed to the ExceptionResponse. <br>"
                            + "book_service client cant find book with your specified book ISBN <br>",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "you didn't provide book ISBN, or provide null book ISBN.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_BOOK_BY_ISBN')")
    @DeleteMapping("/isbn/{isbn}")
    public ResponseEntity<HttpStatus> deleteBookByIsbn(@NotBlank(message = "ISBN can't be blank") @PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}