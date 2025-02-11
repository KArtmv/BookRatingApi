package ua.foxminded.bookrating.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.ISBN;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.FullBookModelAssembler;
import ua.foxminded.bookrating.assembler.RatingModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.model.BookModel;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.service.BookService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/books")
@Validated
@Tag(name = "Books API", description = "Operations relate to books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final FullBookModelAssembler fullBookModelAssembler;
    private final SimpleBookModelAssembler simpleBookModelAssembler;
    private final PagedResourcesAssembler<Book> bookPagedResourcesAssembler;
    private final RatingModelAssembler ratingModelAssembler;
    private final PagedResourcesAssembler<Rating> ratingPagedResourcesAssembler;

    @Operation(summary = "Get book by id", description = "Retrieve a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public BookModel get(@Parameter(name = "id",
            description = "The unique id of the book",
            example = "1")
                 @PathVariable("id") Long id) {
        return fullBookModelAssembler.toModel(bookService.findById(id));
    }

    @Operation(summary = "Create a new book",
            description = "Add a new book",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookModel add(@RequestBody @Valid BookDto bookDto) {
        return fullBookModelAssembler.toModel(bookService.create(bookDto));
    }

    @Operation(summary = "Update an existing book",
            description = "Modify an existing book by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public BookModel update(@Parameter(name = "id",
                            description = "The unique id of the book",
                            example = "1")
                    @PathVariable Long id,
                    @Valid @RequestBody BookDto bookDto) {
        return fullBookModelAssembler.toModel(bookService.update(id, bookDto));
    }

    @Operation(summary = "Delete  book",
            description = "Remove a book by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(name = "id",
            description = "The unique id of the book",
            example = "1")
                       @PathVariable Long id) {
        bookService.delete(id);
    }

    @Operation(summary = "Get ratings for a book",
            description = "Retrieve a paginated list of ratings for specified book id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Book was not found")
    })
    @GetMapping("/{id}/ratings")
    public PagedModel<RatingModel> getBookRatings(@Parameter(name = "id",
                                                      description = "Book id",
                                                      example = "1")
                                                      @PathVariable("id") Long id,
                                                  @ParameterObject @PageableDefault Pageable pageable) {
        return ratingPagedResourcesAssembler.toModel(bookService.getRatingsByBookId(id, pageable), ratingModelAssembler);
    }

    @Operation(summary = "Get all books",
            description = "Retrieve a paginated list of books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter")
    })
    @GetMapping
    public PagedModel<SimpleBookModel> getBooks(@ParameterObject @PageableDefault(sort = "title") Pageable pageable) {
        return bookPagedResourcesAssembler.toModel(bookService.findAll(pageable), simpleBookModelAssembler);
    }

    @Operation(summary = "Get book by ISBN", description = "Retrieve a book by given ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "A book with given ISBN was not found")
    })
    @GetMapping("/isbn")
    public BookModel getBookByIsbn(@Parameter(name = "isbn",
                                            description = "The ISBN of the book to retrieve",
                                            example = "0736688390")
                                       @RequestParam("isbn")
                                       @ISBN(type = ISBN.Type.ANY, message = "The provided ISBN is not valid") String isbn) {
        return fullBookModelAssembler.toModel(bookService.getByIsbn(isbn));
    }

    @Operation(summary = "Get filtered books",
            description = "Retrieve a paginated list of books based on provided filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter")
    })
    @GetMapping("/filter")
    public PagedModel<SimpleBookModel> getFilteredBooks(@Parameter(name = "title",
                                                            description = "A keyword to search within book titles",
                                                            example = "love")
                                                            @RequestParam(value = "title", required = false) String title,
                                                        @Parameter(name = "authorIds",
                                                            description = "List of author ids whose books should be retrieved",
                                                            example = "1,2,3")
                                                            @RequestParam(value = "authorIds", required = false) List<Long> authorIds,
                                                        @Parameter(name = "publisherIds",
                                                            description = "List of publisgers ids whose books should be retrieved",
                                                            example = "1,2,3")
                                                            @RequestParam(value = "publisherIds", required = false) List<Long> publisherIds,
                                                        @Parameter(name = "publicationYear",
                                                            description = "Year of book publication",
                                                            example = "1993")
                                                            @RequestParam(value = "publicationYear", required = false) Integer publicationYear,
                                                        @Parameter(name = "averageRating",
                                                            description = "Minimum average rating of books to retrieve",
                                                            example = "1")
                                                            @RequestParam(value = "averageRating", required = false) Integer averageRating,
                                                        @ParameterObject @PageableDefault Pageable pageable) {
        return bookPagedResourcesAssembler.toModel(
                bookService.getBooksWithFilters(title, authorIds, publisherIds, publicationYear, averageRating, pageable),
                simpleBookModelAssembler
        );
    }

    @Operation(summary = "Restore", description = "Restore soft deleted book by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully restored"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Book was not found")
    })
    @PutMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> restore(@Parameter(name = "id",
            description = "Id of the book to be restored",
            example = "1")
                                        @PathVariable Long id) {
        bookService.restoreById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a deleted book",
            description = "Retrieve soft deleted books by ISBN",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request param"),
            @ApiResponse(responseCode = "404", description = "Book was not found")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/deleted/{isbn}")
    public BookModel getDeletedBook(@Parameter(name = "isbn",
                                        description = "The ISBN of deleted book",
                                        example = "0736688390")
                                        @ISBN(type = ISBN.Type.ANY, message = "The provided ISBN is not valid")
                                        @PathVariable("isbn") String isbn) {
        Book deletedBook = bookService.getDeletedBooksByIsbn(isbn);
        return fullBookModelAssembler.toModel(deletedBook)
                .add(linkTo(methodOn(BookController.class).restore(deletedBook.getId())).withRel("restore"));
    }
}
