package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.persistance.entity.Rating;

public interface RatingService extends CrudService<Rating> {
    Page<Rating> getRatingsByBookId(Long bookId, Pageable pageable);

    Rating save(RatingDto ratingDto);

    Rating update(Long id, Integer newRating);
}
