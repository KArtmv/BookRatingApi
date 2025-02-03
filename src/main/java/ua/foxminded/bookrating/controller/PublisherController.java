package ua.foxminded.bookrating.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.assembler.PublisherModelAssembler;
import ua.foxminded.bookrating.model.PublisherModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.PublisherService;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController extends RestoreController<Publisher, Publisher, PublisherModel> {

    private final PublisherService publisherService;
    private final PublisherModelAssembler publisherModelAssembler;
    private final PagedResourcesAssembler<Publisher> publisherPagedResourcesAssembler;
    private final BookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<BookRatingProjection> bookRatingPagedResourcesAssembler;

    public PublisherController(PublisherService publisherService, PublisherModelAssembler publisherModelAssembler,
                               PagedResourcesAssembler<Publisher> publisherPagedResourcesAssembler,
                               BookModelAssembler bookModelAssembler, PagedResourcesAssembler<BookRatingProjection> bookRatingPagedResourcesAssembler) {
        super(publisherService, publisherModelAssembler);
        this.publisherService = publisherService;
        this.publisherModelAssembler = publisherModelAssembler;
        this.publisherPagedResourcesAssembler = publisherPagedResourcesAssembler;
        this.bookModelAssembler = bookModelAssembler;
        this.bookRatingPagedResourcesAssembler = bookRatingPagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<PublisherModel> getPublishers(@PageableDefault(sort = "name") Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.findAll(pageable), publisherModelAssembler);
    }

    @GetMapping(value = "/{id}/books")
    public PagedModel<SimpleBookModel> getPublisherBooks(@PathVariable Long id,
                                                         @PageableDefault(sort = "book.title") Pageable pageable,
                                                         @RequestParam(value = "desiredAverageRating", required = false, defaultValue = "0") Integer desiredAverageRating) {
        return bookRatingPagedResourcesAssembler.toModel(publisherService.getAllBooksById(id, desiredAverageRating, pageable), bookModelAssembler);
    }

    @GetMapping("/find-by-name")
    public PagedModel<PublisherModel> getPublishersContainName(@NotBlank(message = "Name cannot be blank or empty") @RequestParam("name") String name,
                                                               @PageableDefault Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.getByNameContaining(name, pageable), publisherModelAssembler);
    }
}
