package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.NamedEntity;
import ua.foxminded.bookrating.persistance.repo.ExtendedRepository;
import ua.foxminded.bookrating.service.ExtendedCrudService;

import java.util.Optional;
import java.util.function.Supplier;

@Transactional(readOnly = true)
public abstract class ExtendedCrudServiceImpl<T extends NamedEntity, D> extends PaginatedServiceImpl<T, D> implements ExtendedCrudService<T, D> {

    private final ExtendedRepository<T, Long> extendedRepository;
    private final Supplier<T> entitySupplier;

    protected ExtendedCrudServiceImpl(ExtendedRepository<T, Long> extendedRepository, Supplier<T> entitySupplier) {
        super(extendedRepository);
        this.extendedRepository = extendedRepository;
        this.entitySupplier = entitySupplier;
    }

    public Optional<T> findByName(String name) {
        return extendedRepository.findByName(name);
    }

    public Page<T> getByNameContaining(String name, Pageable pageable) {
        return extendedRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Book> getAllBooksById(Long id, Pageable pageRequest) {
        return extendedRepository.getBooksByEntity(findById(id), pageRequest);
    }

    @Transactional
    public T findByNameOrSave(String name) {
        return extendedRepository.findByName(name).orElseGet(() -> {
            T entity = entitySupplier.get();
            entity.setName(name);
            return extendedRepository.save(entity);
        });
    }

    @Override
    public T getDeletedByName(String name) {
        return extendedRepository.findDeletedByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Deleted " + entitySupplier.get().getClass().getSimpleName().toLowerCase() + " not found with name: " + name
                ));
    }
}
