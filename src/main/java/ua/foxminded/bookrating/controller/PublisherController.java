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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.PublisherModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.dto.AuthorDto;
import ua.foxminded.bookrating.dto.PublisherDto;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.PublisherModel;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.service.PublisherService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/publishers")
@Tag(name = "Publishers API", description = "Operation relate to publishers")
@Validated
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;
    private final PublisherModelAssembler publisherModelAssembler;
    private final PagedResourcesAssembler<Publisher> publisherPagedResourcesAssembler;
    private final SimpleBookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<Book> bookPagedResourcesAssembler;

    @Operation(summary = "Get a publisher by id", description = "Retrieve a publisher by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Publisher not found")
    })
    @GetMapping("/{id}")
    public PublisherModel get(@Parameter(name = "id",
            description = "Publisher id",
            example = "1")
                           @PathVariable("id") Long id) {
        return publisherModelAssembler.toModel(publisherService.findById(id));
    }

    @Operation(summary = "Create a new publisher",
            description = "Add a new publisher",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Publisher successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherModel add(@RequestBody @Valid PublisherDto publisherDto) {
        return publisherModelAssembler.toModel(publisherService.create(publisherDto));
    }

    @Operation(summary = "Update an existing publisher",
            description = "Modify an existing publisher by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Publisher not found")
    })
    @PutMapping("/{id}")
    public PublisherModel update(@Parameter(name = "id",
                                      description = "Publisher id",
                                      example = "1")
                              @PathVariable Long id,
                              @Valid @RequestBody PublisherDto publisherDto) {
        return publisherModelAssembler.toModel(publisherService.update(id, publisherDto));
    }

    @Operation(summary = "Delete a publisher",
            description = "Remove a publisher by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Publisher successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Publisher not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(name = "id",
            description = "Publisher id",
            example = "1")
                       @PathVariable Long id) {
        publisherService.delete(id);
    }

    @Operation(summary = "Get all publishers", description = "Retrieve a paginated list of publishers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter")
    })
    @GetMapping
    public PagedModel<PublisherModel> getPublishers(@ParameterObject @PageableDefault(sort = "name") Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.findAll(pageable), publisherModelAssembler);
    }

    @Operation(summary = "Get boobs by publisher id",
            description = "Retrieve a paginated list of books written by specific publisher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Publisher was not found")
    })
    @GetMapping(value = "/{id}/books")
    public PagedModel<SimpleBookModel> getPublisherBooks(@Parameter(name = "id",
                                                             description = "Publisher id",
                                                             example = "1")
                                                             @PathVariable Long id,
                                                         @ParameterObject @PageableDefault(sort = "book.title") Pageable pageable) {
        return bookPagedResourcesAssembler.toModel(publisherService.getAllBooksById(id, pageable), bookModelAssembler);
    }

    @Operation(summary = "Search publisher by name",
            description = "Retrieve a paginated list of publishers whose name contains given string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter")
    })
    @GetMapping("/find-by-name")
    public PagedModel<PublisherModel> getPublishersContainName(@Parameter(name = "name",
                                                                   description = "A keyword to search within publisher name",
                                                                   example = "HarperCollins")
                                                                   @NotBlank(message = "Name cannot be blank or empty")
                                                                   @RequestParam("name") String name,
                                                               @ParameterObject @PageableDefault Pageable pageable) {
        return publisherPagedResourcesAssembler.toModel(publisherService.getByNameContaining(name, pageable), publisherModelAssembler);
    }


    @Operation(summary = "Restore", description = "Restore soft deleted publisher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully restored"),
            @ApiResponse(responseCode = "400", description = "Invalid id provided"),
            @ApiResponse(responseCode = "404", description = "Publisher was not found")
    })
    @PutMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> restore(@Parameter(name = "id",
            description = "Id of the publisher to be restored",
            example = "1")
                                        @PathVariable Long id) {
        publisherService.restoreById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a deleted publisher",
        description = "Retrieve soft deleted publisher by name",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Publisher was not found")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/deleted")
    public PublisherModel getDeletedPublisher(@NotBlank(message = "Name cannot be blank or empty") @RequestParam("name") String name) {
        Publisher deletedPublisher = publisherService.getDeletedByName(name);
        return publisherModelAssembler.toModel(deletedPublisher)
                .add(linkTo(methodOn(PublisherController.class).restore(deletedPublisher.getId())).withRel("restore"));
    }
}
