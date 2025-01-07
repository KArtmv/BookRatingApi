package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import ua.foxminded.bookrating.persistance.entity.NamedItem;
import ua.foxminded.bookrating.projection.BookRatingProjection;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface ExtendedRepository<T extends NamedItem, ID extends Serializable> extends BaseRepo<T, ID> {

    @Query("""
            SELECT b as book, AVG(r.bookRating) AS averageRating
            FROM #{#entityName} e
            JOIN e.books b
            JOIN b.ratings r
            WHERE e = :entity
            GROUP BY b, e, r.book.title, r.book.publicationYear, r.book.publisher.name
            HAVING AVG(r.bookRating) > :desiredAverageRating
            """)
    Page<BookRatingProjection> getBooksByEntity(@Param("entity") T entity,
                                                @Param("desiredAverageRating") Integer desiredAverageRating,
                                                Pageable pageable);

    Optional<T> findByName(String name);

    @Query("select e from #{#entityName} e")
    Page<T> findAllPaginated(Pageable pageable);

    Page<T> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "select * from #{#entityName} e where e.deleted = true and e.name = :name", nativeQuery = true)
    Optional<T> findDeletedByName(@Param("name") String name);
}
