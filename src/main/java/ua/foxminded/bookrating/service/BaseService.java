package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.projection.BookRatingProjection;

import java.util.List;

public interface BaseService<T extends BaseEntity> extends AbstractService<T> {
    Page<T> findAllPaginated(Pageable pageable);

    Page<BookRatingProjection> getAllBooksById(Long id, Integer desiredAverageRating, Pageable pageRequest);

    Page<T> getByNameContaining(String name, Pageable pageable);
}
