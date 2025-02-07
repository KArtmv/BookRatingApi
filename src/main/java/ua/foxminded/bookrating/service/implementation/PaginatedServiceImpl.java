package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;
import ua.foxminded.bookrating.service.PaginatedService;

public abstract class PaginatedServiceImpl<T extends BaseEntity, D> extends RestoreServiceImpl<T, D> implements PaginatedService<T, D> {

    protected PagingAndSortingRepository<T, Long> pagingAndSortingRepository;

    protected PaginatedServiceImpl(BaseRepository<T, Long> baseRepository) {
        super(baseRepository);
        this.pagingAndSortingRepository = baseRepository;
    }

    public Page<T> findAll(Pageable pageable) {
        return pagingAndSortingRepository.findAll(pageable);
    }
}
