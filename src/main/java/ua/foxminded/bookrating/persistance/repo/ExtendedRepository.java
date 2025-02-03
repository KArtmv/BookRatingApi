package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.NamedEntity;
import ua.foxminded.bookrating.projection.BookRatingProjection;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface ExtendedRepository<T extends NamedEntity, ID extends Serializable> extends BaseRepository<T, ID> {

    @Query("""
            SELECT book
            FROM #{#entityName} e
            JOIN e.books book
            WHERE e = :entity
            """)
    Page<Book> getBooksByEntity(@Param("entity") T entity, Pageable pageable);

    Optional<T> findByName(String name);

    Page<T> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "select * from #{#entityName} e where e.deleted = true and e.name = :name", nativeQuery = true)
    Optional<T> findDeletedByName(@Param("name") String name);
}
