package ua.foxminded.bookrating.service;

import ua.foxminded.bookrating.persistance.entity.BaseEntity;

public interface RestoreService<T extends BaseEntity, D> extends CrudService<T, D> {
    void restoreById(Long id);
}
