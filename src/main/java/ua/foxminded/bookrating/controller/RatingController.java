package ua.foxminded.bookrating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.FullRatingModelAssembler;
import ua.foxminded.bookrating.assembler.RatingModelAssembler;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.service.RatingService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final RatingModelAssembler ratingModelAssembler;
    private final FullRatingModelAssembler fullRatingModelAssembler;
    private final PagedResourcesAssembler<Rating> pagedResourcesAssembler;

    @GetMapping("/ratings/book/{id}")
    public PagedModel<RatingModel> getBookRatings(@PathVariable("id") Long bookId,
                                                  @PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(ratingService.getRatingsByBookId(bookId, pageable), ratingModelAssembler);
    }

    @GetMapping("/ratings/{id}")
    public ResponseEntity<RatingModel> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(fullRatingModelAssembler.toModel(ratingService.findById(id)));
    }

    @PostMapping("/ratings")
    public ResponseEntity<RatingModel> add(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingModelAssembler.toModel(ratingService.save(ratingDto)));
    }

    @PutMapping("/ratings/{id}")
    public ResponseEntity<RatingModel> update(@PathVariable("id") Long id,
                                              @RequestParam("newRating") Integer newRating) {
        return ResponseEntity.ok(fullRatingModelAssembler.toModel(ratingService.update(id, newRating)));
    }

    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        ratingService.delete(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}
