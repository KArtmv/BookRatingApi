package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.projection.BookRatingProjection;

public interface AuthorService extends BaseService<Author> {
}
