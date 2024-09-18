package ua.foxminded.bookrating.service;

import ua.foxminded.bookrating.persistance.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface AbstractService<T extends BaseEntity, ID extends Serializable> {
    T findById(ID id);

    List<T> findAll();

    void save(T entity);

    void delete(T entity);
}
