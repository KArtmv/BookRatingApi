package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.persistance.repo.BaseRepository;

public class PaginatedServiceImpl<T extends BaseEntity> extends RestoreServiceImpl<T> {

    protected PagingAndSortingRepository<T, Long> pagingAndSortingRepository;

    public PaginatedServiceImpl(BaseRepository<T, Long> baseRepository) {
        super(baseRepository);
        this.pagingAndSortingRepository = baseRepository;
    }

    public Page<T> findAll(Pageable pageable) {
        return pagingAndSortingRepository.findAll(pageable);
    }
}
