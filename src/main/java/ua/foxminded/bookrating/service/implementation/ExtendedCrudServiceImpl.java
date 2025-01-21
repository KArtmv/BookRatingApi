package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import org.apache.commons.text.WordUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.NamedEntity;
import ua.foxminded.bookrating.persistance.repo.ExtendedRepository;
import ua.foxminded.bookrating.projection.BookRatingProjection;

@Transactional(readOnly = true)
public class ExtendedCrudServiceImpl<T extends NamedEntity> extends RestoreServiceImpl<T> {

    private final ExtendedRepository<T, Long> extendedRepository;

    public ExtendedCrudServiceImpl(ExtendedRepository<T, Long> extendedRepository) {
        super(extendedRepository);
        this.extendedRepository = extendedRepository;
    }

    public Page<T> findAllPaginated(Pageable pageable) {
        return extendedRepository.findAllPaginated(pageable);
    }

    @Override
    @Transactional
    public T save(T entity) {
        if (extendedRepository.findByName(entity.getName()).isPresent()) {
            throw new EntityExistsException(entity.getName() + " already exists");
        }
        return extendedRepository.save(entity);
    }

    @Override
    @Transactional
    public T update(Long id, T entity) {
        T t = findById(id);
        t.setName(entity.getName());
        return extendedRepository.save(t);
    }

    public Page<T> getByNameContaining(String name, Pageable pageable) {
        return extendedRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<BookRatingProjection> getAllBooksById(Long id, Integer desiredAverageRating, Pageable pageRequest) {
        return extendedRepository.getBooksByEntity(findById(id), desiredAverageRating, pageRequest);
    }

    @Transactional
    public T findOrSave(T entity) {
        return extendedRepository.findByName(entity.getName()).orElseGet(() -> extendedRepository.save(entity));
    }
}
