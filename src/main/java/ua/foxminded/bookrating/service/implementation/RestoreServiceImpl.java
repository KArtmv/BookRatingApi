package ua.foxminded.bookrating.service.implementation;

import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;

public class RestoreServiceImpl<T extends BaseEntity> extends CrudServiceImpl<T> {

    private final BaseRepository<T, Long> baseRepository;

    protected RestoreServiceImpl(BaseRepository<T, Long> baseRepository) {
        super(baseRepository);
        this.baseRepository = baseRepository;
    }

    public void restoreById(Long id) {
        baseRepository.restore(id);
    }
}
