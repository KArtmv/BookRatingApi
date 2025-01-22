package ua.foxminded.bookrating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.assembler.FullRatingModelAssembler;
import ua.foxminded.bookrating.assembler.RatingModelAssembler;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.service.RatingService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final FullRatingModelAssembler fullRatingModelAssembler;

    @GetMapping("/ratings/{id}")
    public RatingModel get(@PathVariable("id") Long id) {
        return fullRatingModelAssembler.toModel(ratingService.findById(id));
    }

    @PostMapping("/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public RatingModel add(@RequestBody RatingDto ratingDto) {
        return fullRatingModelAssembler.toModel(ratingService.save(ratingDto));
    }

    @PutMapping("/ratings/{id}")
    public RatingModel update(@PathVariable("id") Long id,
                              @RequestBody RatingDto dto) {
        return fullRatingModelAssembler.toModel(ratingService.update(id, dto));
    }

    @DeleteMapping("/ratings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        ratingService.delete(id);
    }
}
