package ua.foxminded.bookrating.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.RatingOfBookModelAssembler;
import ua.foxminded.bookrating.assembler.UserModelAssembler;
import ua.foxminded.bookrating.dto.AuthorDto;
import ua.foxminded.bookrating.dto.UserDto;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.model.UserModel;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "Users API", description = "Operations relate to users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RatingOfBookModelAssembler ratingModelAssembler;
    private final PagedResourcesAssembler<Rating> pagedResourcesAssembler;
    private final UserModelAssembler userModelAssembler;

    @Operation(summary = "Get an user by id", description = "Retrieve an user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public UserModel get(@Parameter(name = "id",
            description = "User id",
            example = "1")
                           @PathVariable("id") Long id) {
        return userModelAssembler.toModel(userService.findById(id));
    }

    @Operation(summary = "Create a new user",
            description = "Add a new user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel add(@RequestBody @Valid UserDto userDto) {
        return userModelAssembler.toModel(userService.create(userDto));
    }

    @Operation(summary = "Update an existing user",
            description = "Modify an existing user by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public UserModel update(@Parameter(name = "id",
                                      description = "User id",
                                      example = "1")
                              @PathVariable Long id,
                              @Valid @RequestBody UserDto userDto) {
        return userModelAssembler.toModel(userService.update(id, userDto));
    }

    @Operation(summary = "Delete an user",
            description = "Remove an user by its id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(name = "id",
            description = "User id",
            example = "1")
                       @PathVariable Long id) {
        userService.delete(id);
    }

    @Operation(summary = "Restore", description = "Restore soft deleted entityty by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully restored"),
            @ApiResponse(responseCode = "400", description = "Invalid id provided"),
            @ApiResponse(responseCode = "404", description = "Entityty was not found")
    })
    @PutMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> restore(@Parameter(name = "id",
            description = "Id of the entityty to be restored",
            example = "1")
                                        @PathVariable Long id) {
        userService.restoreById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get rated books by user id",
        description = "Retrieve a paginated list of ratings by specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "404", description = "User wan not found")
    })
    @GetMapping("/{id}/rated-books")
    public PagedModel<RatingModel> getRatedBooksByUser(@Parameter(name = "id",
                                                           description = "User id",
                                                           example = "1")
                                                           @PathVariable("id") Long userId,
                                                       @ParameterObject @PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(userService.findRatedBooksByUser(userId, pageable), ratingModelAssembler);
    }
}
