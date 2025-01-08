
package ua.foxminded.bookrating.service;

import ua.foxminded.bookrating.persistance.entity.BaseEntity;

public interface RestoreService<T extends BaseEntity> extends CrudService<T> {
    void restoreById(Long id);
}
