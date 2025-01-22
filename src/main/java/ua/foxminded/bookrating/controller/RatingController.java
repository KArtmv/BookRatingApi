package ua.foxminded.bookrating.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.bookrating.assembler.FullRatingModelAssembler;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.service.RatingService;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController extends CrudController<Rating, RatingDto, RatingModel> {

    public RatingController(RatingService ratingService,
                            FullRatingModelAssembler fullRatingModelAssembler) {
        super(ratingService, fullRatingModelAssembler);
    }
}
