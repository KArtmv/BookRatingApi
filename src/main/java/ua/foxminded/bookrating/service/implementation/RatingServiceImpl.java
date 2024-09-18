package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.service.RatingService;

@Service
public class RatingServiceImpl extends AbstractServiceImpl<Rating> implements RatingService {

    public RatingServiceImpl(JpaRepository<Rating, Long> repository) {
        super(repository);
    }
}
