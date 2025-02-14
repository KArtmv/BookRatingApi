package ua.foxminded.bookrating.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.repo.RatingRepository;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.RatingService;
import ua.foxminded.bookrating.service.UserService;

@Service
@Transactional(readOnly = true)
public class RatingServiceImpl extends CrudServiceImpl<Rating, RatingDto> implements RatingService {

    private final RatingRepository ratingRepository;
    private final BookService bookService;
    private final UserService userService;

    public RatingServiceImpl(RatingRepository ratingRepository, BookService bookService, UserService userService) {
        super(ratingRepository);
        this.ratingRepository = ratingRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Rating create(RatingDto ratingDto) {
        return ratingRepository.save(toEntity(ratingDto, new Rating()));
    }

    @Transactional
    @Override
    public Rating update(Long id, RatingDto ratingDto) {
        Rating rating = findById(id);
        rating.setBookRating(ratingDto.bookRating());
        return ratingRepository.save(rating);
    }

    private Rating toEntity(RatingDto ratingDto, Rating rating) {
        rating.setUser(userService.findById(ratingDto.userId()));
        rating.setBook(bookService.findById(ratingDto.bookId()));
        rating.setBookRating(ratingDto.bookRating());
        return rating;
    }
}
