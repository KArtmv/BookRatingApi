package ua.foxminded.bookrating.util.rating;

import lombok.Getter;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.user.UserData;

@Getter
public class RatingData {

    public static final BookData BOOK_DATA = new BookData();
    public static final UserData USER_DATA = new UserData();

    private final Long id = 220481L;
    private final Integer userRating = 7;
    private final Integer updatedRating = 5;
    private final Rating newRating = new Rating(BOOK_DATA.getBook(), USER_DATA.getUser(), userRating);
    private final Rating rating = new Rating(id, BOOK_DATA.getBook(), USER_DATA.getUser(), userRating);
    private final RatingDto ratingDto = new RatingDto(BOOK_DATA.getId(), USER_DATA.getId(), userRating);
}
