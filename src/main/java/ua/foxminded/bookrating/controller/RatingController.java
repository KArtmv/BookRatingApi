package ua.foxminded.bookrating.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.FullRatingModelAssembler;
import ua.foxminded.bookrating.dto.AuthorDto;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.service.RatingService;

@RestController
@RequestMapping("/api/v1/ratings")
@Tag(name = "Ratings API", description = "Operation relate to ratings")
@Validated
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final FullRatingModelAssembler fullRatingModelAssembler;

    @Operation(summary = "Get a rating by id", description = "Retrieve a rating by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @GetMapping("/{id}")
    public RatingModel get(@Parameter(name = "id",
            description = "Rating id",
            example = "1")
                           @PathVariable("id") Long id) {
        return fullRatingModelAssembler.toModel(ratingService.findById(id));
    }

    @Operation(summary = "Create a new rating",
            description = "Add a new rating",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rating successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingModel add(@RequestBody @Valid RatingDto ratingDto) {
        return fullRatingModelAssembler.toModel(ratingService.create(ratingDto));
    }

    @Operation(summary = "Update an existing rating",
            description = "Modify an existing rating by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @PutMapping("/{id}")
    public RatingModel update(@Parameter(name = "id",
                                      description = "Rating id",
                                      example = "1")
                              @PathVariable Long id,
                              @Valid @RequestBody RatingDto ratingDto) {
        return fullRatingModelAssembler.toModel(ratingService.update(id, ratingDto));
    }

    @Operation(summary = "Delete a rating",
            description = "Remove a rating by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rating successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(name = "id",
            description = "Rating id",
            example = "1")
                       @PathVariable Long id) {
        ratingService.delete(id);
    }
}
