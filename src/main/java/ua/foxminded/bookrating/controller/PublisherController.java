package ua.foxminded.bookrating.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.PublisherModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.dto.PublisherDto;
import ua.foxminded.bookrating.model.PublisherModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.service.PublisherService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController extends RestoreController<Publisher, PublisherDto, PublisherModel> {

    private final PublisherService publisherService;
    private final PublisherModelAssembler publisherModelAssembler;
    private final PagedResourcesAssembler<Publisher> publisherPagedResourcesAssembler;
    private final SimpleBookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<Book> bookPagedResourcesAssembler;

    public PublisherController(PublisherService publisherService, PublisherModelAssembler publisherModelAssembler,
                               PagedResourcesAssembler<Publisher> publisherPagedResourcesAssembler,
                               SimpleBookModelAssembler bookModelAssembler, PagedResourcesAssembler<Book> bookPagedResourcesAssembler) {
        super(publisherService, publisherModelAssembler);
        this.publisherService = publisherService;
        this.publisherModelAssembler = publisherModelAssembler;
        this.publisherPagedResourcesAssembler = publisherPagedResourcesAssembler;
        this.bookModelAssembler = bookModelAssembler;
        this.bookPagedResourcesAssembler = bookPagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<PublisherModel> getPublishers(@PageableDefault(sort = "name") Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.findAll(pageable), publisherModelAssembler);
    }

    @GetMapping(value = "/{id}/books")
    public PagedModel<SimpleBookModel> getPublisherBooks(@PathVariable Long id,
                                                         @PageableDefault(sort = "book.title") Pageable pageable) {
        return bookPagedResourcesAssembler.toModel(publisherService.getAllBooksById(id, pageable), bookModelAssembler);
    }

    @GetMapping("/find-by-name")
    public PagedModel<PublisherModel> getPublishersContainName(@NotBlank(message = "Name cannot be blank or empty") @RequestParam("name") String name,
                                                               @PageableDefault Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.getByNameContaining(name, pageable), publisherModelAssembler);
    }

    @GetMapping("/deleted")
    public PublisherModel getDeletedPublisher(@RequestParam("name") String name) {
        Publisher deletedPublisher = publisherService.getDeletedByName(name);
        return publisherModelAssembler.toModel(deletedPublisher)
                .add(linkTo(methodOn(PublisherController.class).restore(deletedPublisher.getId())).withRel("restore"));
    }
}
