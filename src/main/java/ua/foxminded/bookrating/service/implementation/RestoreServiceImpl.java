package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;

public class RestoreServiceImpl<T extends BaseEntity> extends CrudServiceImpl<T> {

    private final BaseRepository<T, Long> repository;

    protected RestoreServiceImpl(BaseRepository<T, Long> repository) {
        super(repository);
        this.repository = repository;
    }

    public void restoreById(Long id) {
        repository.restore(id);
    }
}
