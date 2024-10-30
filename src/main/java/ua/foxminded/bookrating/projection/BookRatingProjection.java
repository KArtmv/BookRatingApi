package ua.foxminded.bookrating.projection;

import ua.foxminded.bookrating.persistance.entity.Book;

public interface BookRatingProjection {
    Book getBook();
    Double getAverageRating();
}
