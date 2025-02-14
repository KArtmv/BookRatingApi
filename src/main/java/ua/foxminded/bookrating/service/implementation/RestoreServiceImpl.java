package ua.foxminded.bookrating.service.implementation;

import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;
import ua.foxminded.bookrating.service.RestoreService;

public abstract class RestoreServiceImpl<T extends BaseEntity, D> extends CrudServiceImpl<T, D> implements RestoreService<T, D> {

    private final BaseRepository<T, Long> baseRepository;

    protected RestoreServiceImpl(BaseRepository<T, Long> baseRepository) {
        super(baseRepository);
        this.baseRepository = baseRepository;
    }

    public void restoreById(Long id) {
        baseRepository.restore(id);
    }
}
