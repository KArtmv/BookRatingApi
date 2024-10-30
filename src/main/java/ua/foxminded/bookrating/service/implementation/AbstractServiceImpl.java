package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;

import java.util.List;

@Transactional(readOnly = true)
public class AbstractServiceImpl<T extends BaseEntity> {

    protected final JpaRepository<T, Long> repository;

    protected AbstractServiceImpl(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public T findById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Transactional
    public T update(Long id, T entity) {
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }
}
