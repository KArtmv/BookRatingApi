package ua.foxminded.bookrating.service;

import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.persistance.entity.Rating;

public interface RatingService extends CrudService<Rating, RatingDto> {
}
