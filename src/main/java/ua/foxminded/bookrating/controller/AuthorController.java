package ua.foxminded.bookrating.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.AuthorModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.dto.AuthorDto;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.service.AuthorService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/authors")
@Tag(name = "Authors API", description = "Operations relate to authors")
@Validated
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorModelAssembler authorModelAssembler;
    private final PagedResourcesAssembler<Author> authorPagedResourcesAssembler;
    private final SimpleBookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<Book> bookPagedResourcesAssembler;

    @Operation(summary = "Get an author by id", description = "Retrieve an author by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    public AuthorModel get(@Parameter(name = "id",
            description = "Author id",
            example = "1")
                 @PathVariable("id") Long id) {
        return authorModelAssembler.toModel(authorService.findById(id));
    }

    @Operation(summary = "Create a new author",
            description = "Add a new author",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorModel add(@RequestBody @Valid AuthorDto authorDto) {
        return authorModelAssembler.toModel(authorService.create(authorDto));
    }

    @Operation(summary = "Update an existing author",
            description = "Modify an existing author by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PutMapping("/{id}")
    public AuthorModel update(@Parameter(name = "id",
                            description = "Author id",
                            example = "1")
                    @PathVariable Long id,
                    @Valid @RequestBody AuthorDto authorDto) {
        return authorModelAssembler.toModel(authorService.update(id, authorDto));
    }

    @Operation(summary = "Delete an author",
            description = "Remove an author by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(name = "id",
            description = "Author id",
            example = "1")
                       @PathVariable Long id) {
        authorService.delete(id);
    }

    @Operation(summary = "Get all authors", description = "Retrieve a paginated list of authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter")
    })
    @GetMapping
    public PagedModel<AuthorModel> getAll(@ParameterObject @PageableDefault(sort = "name") Pageable pageable) {
        return authorPagedResourcesAssembler.toModel(authorService.findAll(pageable), authorModelAssembler);
    }

    @Operation(summary = "Get books by author id",
            description = "Retrieve a paginated list of books written by specific author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Author was not found")
    })
    @GetMapping("/{id}/books")
    public PagedModel<SimpleBookModel> getAuthorBooks(@Parameter(name = "id",
                                                              description = "Author id",
                                                              example = "1")
                                                      @PathVariable Long id,
                                                      @ParameterObject
                                                      @PageableDefault(sort = "book.title") Pageable pageable) {
        return bookPagedResourcesAssembler.toModel(authorService.getAllBooksById(id, pageable), bookModelAssembler);
    }

    @Operation(summary = "Search authors by name",
            description = "Retrieve a paginated list of authors whose name contains given string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter")
    })
    @GetMapping("/find-by-name")
    public PagedModel<AuthorModel> getAuthorsContainName(@Parameter(name = "name",
                                                                 description = "A keyword to search within author name",
                                                                 example = "William")
                                                         @NotBlank(message = "Name cannot be blank or empty")
                                                         @RequestParam("name") String name,
                                                         @ParameterObject @PageableDefault Pageable pageable) {
        return authorPagedResourcesAssembler.toModel(authorService.getByNameContaining(name, pageable), authorModelAssembler);
    }

    @Operation(summary = "Restore", description = "Restore soft deleted author by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully restored"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Author was not found")
    })
    @PutMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> restore(@Parameter(name = "id",
            description = "Id of the author to be restored",
            example = "1")
                                        @PathVariable Long id) {
        authorService.restoreById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a deleted author",
            description = "Retrieve soft deleted author by name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Author was not found")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/deleted")
    public AuthorModel getDeletedAuthor(@Parameter(name = "name",
                                                description = "Author name",
                                                example = "William")
                                        @NotBlank(message = "Name cannot be blank or empty")
                                        @RequestParam("name") String name) {
        Author deletedAuthor = authorService.getDeletedByName(name);
        return authorModelAssembler.toModel(deletedAuthor)
                .add(linkTo(methodOn(AuthorController.class).restore(deletedAuthor.getId())).withRel("restore"));
    }
}
