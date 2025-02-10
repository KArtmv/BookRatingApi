package ua.foxminded.bookrating.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.bookrating.assembler.RatingOfBookModelAssembler;
import ua.foxminded.bookrating.assembler.UserModelAssembler;
import ua.foxminded.bookrating.dto.UserDto;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.model.UserModel;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController extends RestoreController<User, UserDto, UserModel> {

    private final UserService userService;
    private final RatingOfBookModelAssembler ratingModelAssembler;
    private final PagedResourcesAssembler<Rating> pagedResourcesAssembler;

    public UserController(UserService userService, RatingOfBookModelAssembler ratingModelAssembler,
                          UserModelAssembler userModelAssembler, PagedResourcesAssembler<Rating> pagedResourcesAssembler) {
        super(userService, userModelAssembler);
        this.userService = userService;
        this.ratingModelAssembler = ratingModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}/rated-books")
    public PagedModel<RatingModel> getRatedBooksByUser(@PathVariable("id") Long userId, @PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(userService.findRatedBooksByUser(userId, pageable), ratingModelAssembler);
    }
}
