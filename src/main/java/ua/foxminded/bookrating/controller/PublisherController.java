package ua.foxminded.bookrating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.assembler.PublisherModelAssembler;
import ua.foxminded.bookrating.model.PublisherModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.PublisherService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;
    private final PublisherModelAssembler publisherModelAssembler;
    private final PagedResourcesAssembler<Publisher> publisherPagedResourcesAssembler;
    private final BookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<BookRatingProjection> bookRatingPagedResourcesAssembler;

    @GetMapping("/publishers")
    public PagedModel<PublisherModel> getPublishers(@PageableDefault(sort = "name") Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.findAllPaginated(pageable), publisherModelAssembler);
    }

    @GetMapping(value = "/publishers/{id}/books")
    public PagedModel<SimpleBookModel> getPublisherBooks(@PathVariable Long id,
                                                         @PageableDefault(sort = "title") Pageable pageable,
                                                         @RequestParam(value = "desiredAverageRating", required = false, defaultValue = "0") Integer desiredAverageRating) {
        return bookRatingPagedResourcesAssembler.toModel(publisherService.getAllBooksById(id, desiredAverageRating, pageable), bookModelAssembler);
    }

    @GetMapping("/publishers/find-by-name")
    public PagedModel<PublisherModel> getPublishersContainName(@RequestParam("name") String name, @PageableDefault Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.getByNameContaining(name, pageable), publisherModelAssembler);
    }

    @GetMapping("/publishers/{id}")
    public PublisherModel getPublisher(@PathVariable Long id) {
        return publisherModelAssembler.toModel(publisherService.findById(id));
    }

    @PostMapping("/publishers")
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherModel add(@RequestBody Publisher publisher) {
        return publisherModelAssembler.toModel(publisherService.save(publisher));
    }

    @PutMapping("/publishers/{id}")
    public PublisherModel update(@PathVariable Long id, @RequestBody Publisher publisher) {
        return publisherModelAssembler.toModel(publisherService.update(id, publisher));
    }

    @DeleteMapping("/publishers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        publisherService.delete(id);
    }
}
