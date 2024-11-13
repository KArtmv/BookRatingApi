package ua.foxminded.bookrating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.AuthorModelAssembler;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.AuthorService;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorModelAssembler authorModelAssembler;
    private final PagedResourcesAssembler<Author> authorPagedResourcesAssembler;
    private final BookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<BookRatingProjection> bookRatingPagedResourcesAssembler;

    @GetMapping("/authors")
    public PagedModel<AuthorModel> getAll(@PageableDefault(sort = "name") Pageable pageable) {
        return authorPagedResourcesAssembler.toModel(authorService.findAllPaginated(pageable), authorModelAssembler);
    }

    @GetMapping("/authors/{id}/books")
    public PagedModel<SimpleBookModel> getAuthorBooks(@PathVariable Long id,
                                                      @PageableDefault(sort = "book.title") Pageable pageable,
                                                      @RequestParam(value = "desiredAverageRating", required = false, defaultValue = "0") Integer desiredAverageRating) {
        return bookRatingPagedResourcesAssembler.toModel(authorService.getAllBooksById(id, desiredAverageRating, pageable), bookModelAssembler);
    }

    @GetMapping("/authors/find-by-name")
    public PagedModel<AuthorModel> getAuthorsContainName(@RequestParam("name") String name, @PageableDefault Pageable pageable) {
        return authorPagedResourcesAssembler.toModel(authorService.getByNameContaining(name, pageable), authorModelAssembler);
    }

    @GetMapping("/authors/{id}")
    public AuthorModel get(@PathVariable("id") Long id) {
        return authorModelAssembler.toModel(authorService.findById(id));
    }

    @PostMapping("/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorModel add(@RequestBody Author author) {
        return authorModelAssembler.toModel(authorService.save(author));
    }

    @PutMapping("/authors/{id}")
    public AuthorModel update(@PathVariable Long id, @RequestBody Author author) {
        return authorModelAssembler.toModel(authorService.update(id, author));
    }

    @DeleteMapping("/authors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
