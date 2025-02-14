package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;

public interface PaginatedService<T extends BaseEntity, D> extends RestoreService<T, D> {
    Page<T> findAll(Pageable pageable);
}
