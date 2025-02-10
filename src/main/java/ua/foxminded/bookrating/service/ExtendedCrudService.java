package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.entity.Book;

public interface ExtendedCrudService<T extends BaseEntity, D> extends PaginatedService<T, D> {
    Page<Book> getAllBooksById(Long id, Pageable pageRequest);

    Page<T> getByNameContaining(String name, Pageable pageable);

    T findByNameOrSave(String name);

    T getDeletedByName(String name);
}
