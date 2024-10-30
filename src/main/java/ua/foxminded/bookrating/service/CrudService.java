package ua.foxminded.bookrating.service;

import ua.foxminded.bookrating.persistance.entity.BaseEntity;

import java.util.List;

public interface CrudService<T extends BaseEntity> {
    T findById(Long id);

    List<T> findAll();

    void delete(Long id);

    T update(Long id, T entity);

    T save(T entity);
}
