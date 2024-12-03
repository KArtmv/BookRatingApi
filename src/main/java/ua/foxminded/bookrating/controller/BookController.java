package ua.foxminded.bookrating.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.assembler.FullBookModelAssembler;
import ua.foxminded.bookrating.assembler.RatingModelAssembler;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.dto.BookFilterRequest;
import ua.foxminded.bookrating.model.BookModel;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.BookService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class BookController {

    private final BookService bookService;
    private final FullBookModelAssembler fullBookModelAssembler;
    private final BookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<BookRatingProjection> pagedResourcesAssembler;
    private final RatingModelAssembler ratingModelAssembler;
    private final PagedResourcesAssembler<Rating> ratingPagedResourcesAssembler;

    @GetMapping("/books/{id}/ratings")
    public PagedModel<RatingModel> getBookRatings(@PathVariable("id") Long id,
                                                  @PageableDefault Pageable pageable) {
        return ratingPagedResourcesAssembler.toModel(bookService.getRatingsByBookId(id, pageable), ratingModelAssembler);
    }

    @GetMapping("/books")
    public PagedModel<SimpleBookModel> getBooks(@PageableDefault Pageable pageable,
                                                @RequestParam(value = "averageRating", required = false, defaultValue = "0") Integer averageRating) {
        return pagedResourcesAssembler.toModel(bookService.findAllPaginated(averageRating, pageable), bookModelAssembler);
    }

    @GetMapping("/books/{id}")
    public BookModel getBook(@PathVariable Long id) {
        return fullBookModelAssembler.toModel(bookService.findById(id));
    }

    @GetMapping("/books/isbn")
    public BookModel getBookByIsbn(@RequestParam("isbn") @ISBN(type = ISBN.Type.ANY, message = "The provided ISBN is not valid") String isbn) {
        return fullBookModelAssembler.toModel(bookService.getByIsbn(isbn));
    }

    @GetMapping("/books/title")
    public PagedModel<SimpleBookModel> getBooksContainTitle(@RequestParam("title") @NotBlank(message = "The title cannot be blank or empty") String title, @PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(bookService.getByTitleContaining(title, pageable), bookModelAssembler);
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookModel add(@Valid @RequestBody BookDto bookDto) {
        return fullBookModelAssembler.toModel(bookService.save(bookDto));
    }

    @PutMapping("/books/{id}")
    public BookModel update(@PathVariable Long id, @Valid @RequestBody BookDto book) {
        return fullBookModelAssembler.toModel(bookService.update(id, book));
    }

    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/books/filter-by")
    public PagedModel<SimpleBookModel> getFilteredBooks(@Valid @ModelAttribute BookFilterRequest filterRequest,
                                                        @RequestParam(value = "averageRating", required = false, defaultValue = "0") Integer averageRating,
                                                        @RequestParam(required = false, defaultValue = "") String title,
                                                        @PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(
                bookService.getBooksByAuthorAndPublisher(
                        filterRequest.authorsId(),
                        filterRequest.publishersId(),
                        averageRating,
                        title,
                        pageable
                ),
                bookModelAssembler
        );
    }
}
