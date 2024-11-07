package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.exception.ItemNotFoundException;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.entity.NamedItem;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;

@Transactional(readOnly = true)
public class ExtendedCrudServiceImpl<T extends NamedItem> extends CrudServiceImpl<T> {

    private final BaseRepository<T, Long> baseRepository;

    public ExtendedCrudServiceImpl(BaseRepository<T, Long> baseRepository) {
        super(baseRepository);
        this.baseRepository = baseRepository;
    }

    public Page<T> findAllPaginated(Pageable pageable) {
        return baseRepository.findAllPaginated(pageable);
    }

    public T findByName(String name) {
        return baseRepository.findByName(name).orElseThrow(() -> new ItemNotFoundException("Item not found by name: " + name));
    }

    public boolean existsByName(String name) {
        return baseRepository.existsByName(name);
    }

    @Override
    @Transactional
    public T save(T entity) {
        if (baseRepository.findByName(entity.getName()).isPresent()) {
            throw new EntityExistsException(entity.getName() + "{} already exists");
        }
        return  baseRepository.save(entity);
    }

    @Override
    @Transactional
    public T update(Long id, T entity) {
        T t = findById(id);
        t.setName(entity.getName());
        return baseRepository.save(t);
    }

    public Page<T> getByNameContaining(String name, Pageable pageable) {
        return baseRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
