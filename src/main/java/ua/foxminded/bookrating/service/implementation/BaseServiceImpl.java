package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;

@Transactional(readOnly = true)
public class BaseServiceImpl<T extends BaseEntity> extends CrudServiceImpl<T> {

    private final BaseRepository<T, Long> baseRepository;

    public BaseServiceImpl(BaseRepository<T, Long> baseRepository) {
        super(baseRepository);
        this.baseRepository = baseRepository;
    }

    public Page<T> findAllPaginated(Pageable pageable) {
        return baseRepository.findAllPaginated(pageable);
    }

    public T findByName(String name) {
        return baseRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    public boolean existsByName(String name) {
        return baseRepository.existsByName(name);
    }

    public Page<T> getByNameContaining(String name, Pageable pageable) {
        return baseRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
