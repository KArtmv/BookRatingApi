package ua.foxminded.bookrating.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.AuthorModelAssembler;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.AuthorService;

@RestController
@RequestMapping(value = "/api/v1/authors")
public class AuthorController extends RestoreController<Author, Author, AuthorModel> {

    private final AuthorService authorService;
    private final AuthorModelAssembler authorModelAssembler;
    private final PagedResourcesAssembler<Author> authorPagedResourcesAssembler;
    private final SimpleBookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<Book> bookRatingPagedResourcesAssembler;

    public AuthorController(AuthorService authorService, AuthorModelAssembler authorModelAssembler,
                            PagedResourcesAssembler<Author> authorPagedResourcesAssembler,
                            SimpleBookModelAssembler bookModelAssembler, PagedResourcesAssembler<Book> bookRatingPagedResourcesAssembler) {
        super(authorService, authorModelAssembler);
        this.authorService = authorService;
        this.authorModelAssembler = authorModelAssembler;
        this.authorPagedResourcesAssembler = authorPagedResourcesAssembler;
        this.bookModelAssembler = bookModelAssembler;
        this.bookRatingPagedResourcesAssembler = bookRatingPagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<AuthorModel> getAll(@PageableDefault(sort = "name") Pageable pageable) {
        return authorPagedResourcesAssembler.toModel(authorService.findAll(pageable), authorModelAssembler);
    }

    @GetMapping("/{id}/books")
    public PagedModel<SimpleBookModel> getAuthorBooks(@PathVariable Long id,
                                                      @PageableDefault(sort = "book.title") Pageable pageable) {
        return bookRatingPagedResourcesAssembler.toModel(authorService.getAllBooksById(id, pageable), bookModelAssembler);
    }

    @GetMapping("/find-by-name")
    public PagedModel<AuthorModel> getAuthorsContainName(@NotBlank(message = "Name cannot be blank or empty") @RequestParam("name") String name,
                                                         @PageableDefault Pageable pageable) {
        return authorPagedResourcesAssembler.toModel(authorService.getByNameContaining(name, pageable), authorModelAssembler);
    }
}
