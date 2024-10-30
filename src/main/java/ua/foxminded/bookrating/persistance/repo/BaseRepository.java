package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {

    Optional<T> findByName(String name);

    @Query("select e from #{#entityName} e")
    Page<T> findAllPaginated(Pageable pageable);

    boolean existsByName(String name);

    Page<T> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
