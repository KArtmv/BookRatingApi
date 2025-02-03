package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;

public class PaginatedServiceImpl<T extends BaseEntity> extends RestoreServiceImpl<T> {

    protected BaseRepository<T, Long> repository;

    public PaginatedServiceImpl(BaseRepository<T, Long> repository) {
        super(repository);
        this.repository = repository;
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
