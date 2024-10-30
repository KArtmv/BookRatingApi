package ua.foxminded.bookrating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.RatingOfBookModelAssembler;
import ua.foxminded.bookrating.assembler.UserModelAssembler;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.model.UserModel;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.service.UserService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RatingOfBookModelAssembler ratingModelAssembler;
    private final UserModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<Rating> pagedResourcesAssembler;

    @GetMapping("/users/{id}/rated-books")
    public PagedModel<RatingModel> getRatedBooksByUser(@PathVariable("id") Long userId, @PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(userService.findRatedBooksByUser(userId, pageable), ratingModelAssembler);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserModel> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userModelAssembler.toModel(userService.findById(id)));
    }

    @PostMapping("/users")
    public ResponseEntity<UserModel> add(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userModelAssembler.toModel(userService.save(user)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserModel> update(@PathVariable("id") Long id, @RequestBody User user) {
        return ResponseEntity.ok(userModelAssembler.toModel(userService.update(id, user)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
