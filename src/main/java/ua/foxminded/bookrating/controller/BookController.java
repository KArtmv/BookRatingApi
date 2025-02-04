package ua.foxminded.bookrating.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.assembler.FullBookModelAssembler;
import ua.foxminded.bookrating.assembler.RatingModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.dto.BookFilterRequest;
import ua.foxminded.bookrating.model.BookModel;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Validated
public class BookController extends RestoreController<Book, BookDto, BookModel> {

    private final BookService bookService;
    private final FullBookModelAssembler fullBookModelAssembler;
    private final SimpleBookModelAssembler simpleBookModelAssembler;
    private final PagedResourcesAssembler<Book> bookPagedResourcesAssembler;
    private final RatingModelAssembler ratingModelAssembler;
    private final PagedResourcesAssembler<Rating> ratingPagedResourcesAssembler;

    public BookController(BookService bookService, FullBookModelAssembler fullBookModelAssembler,
                           SimpleBookModelAssembler simpleBookModelAssembler, PagedResourcesAssembler<Book> bookPagedResourcesAssembler,
                          RatingModelAssembler ratingModelAssembler, PagedResourcesAssembler<Rating> ratingPagedResourcesAssembler) {
        super(bookService, fullBookModelAssembler);
        this.bookService = bookService;
        this.fullBookModelAssembler = fullBookModelAssembler;
        this.simpleBookModelAssembler = simpleBookModelAssembler;
        this.bookPagedResourcesAssembler = bookPagedResourcesAssembler;
        this.ratingModelAssembler = ratingModelAssembler;
        this.ratingPagedResourcesAssembler = ratingPagedResourcesAssembler;
    }

    @GetMapping("/{id}/ratings")
    public PagedModel<RatingModel> getBookRatings(@PathVariable("id") Long id,
                                                  @PageableDefault Pageable pageable) {
        return ratingPagedResourcesAssembler.toModel(bookService.getRatingsByBookId(id, pageable), ratingModelAssembler);
    }

    @GetMapping
    public PagedModel<SimpleBookModel> getBooks(@PageableDefault(sort = "title") Pageable pageable) {
        return bookPagedResourcesAssembler.toModel(bookService.findAll(pageable), simpleBookModelAssembler);
    }

    @GetMapping("/isbn")
    public BookModel getBookByIsbn(@RequestParam("isbn") @ISBN(type = ISBN.Type.ANY, message = "The provided ISBN is not valid") String isbn) {
        return fullBookModelAssembler.toModel(bookService.getByIsbn(isbn));
    }

    @GetMapping("/filter")
    public PagedModel<SimpleBookModel> getFilteredBooks(@RequestParam(value = "title", required = false) String title,
                                                        @RequestParam(value = "authorIds", required = false) List<Long> authorIds,
                                                        @RequestParam(value = "publisherIds", required = false) List<Long> publisherIds,
                                                        @RequestParam(value = "publicationYear", required = false) Integer publicationYear,
                                                        @RequestParam(value = "averageRating", required = false) Integer averageRating,
                                                        @PageableDefault Pageable pageable) {
        return bookPagedResourcesAssembler.toModel(
                bookService.getBooksWithFilters(title, authorIds, publisherIds, publicationYear, averageRating, pageable),
                simpleBookModelAssembler
        );
    }
}
