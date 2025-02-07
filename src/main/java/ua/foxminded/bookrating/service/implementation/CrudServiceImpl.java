package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.service.CrudService;

import java.util.List;

@Transactional(readOnly = true)
public abstract class CrudServiceImpl<T extends BaseEntity, D> implements CrudService<T, D> {

    protected final JpaRepository<T, Long> repository;

    protected CrudServiceImpl(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public T findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " is not found"));
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }
}
