package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.implementation.RestoreServiceImpl;

import java.util.Optional;

public interface ExtendedCrudService<T extends BaseEntity, D> extends RestoreService<T, D> {
    Page<T> findAllPaginated(Pageable pageable);

    Page<BookRatingProjection> getAllBooksById(Long id, Integer desiredAverageRating, Pageable pageRequest);

    Page<T> getByNameContaining(String name, Pageable pageable);

    T findOrSave(T entity);
}
