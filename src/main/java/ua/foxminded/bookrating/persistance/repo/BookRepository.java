package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.projection.BookRatingProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("""
            select b as book, AVG(r.bookRating) AS averageRating
                from Book b
                join b.ratings r
                group by b.id
                having AVG(r.bookRating) >= :desiredAverageRating
            """)
    Page<BookRatingProjection> findAllPaginated(@Param("desiredAverageRating") Integer desiredAverageRating, Pageable pageable);

    @Query("""
            select b as book, AVG(r.bookRating) AS averageRating
            from Book b
            join b.ratings r
            where lower(b.title)
            like lower(concat('%', :title, '%'))
            group by b.id
            """)
    Page<BookRatingProjection> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    @Query("""
       select b as book, AVG(r.bookRating) AS averageRating
       from Book b
       join b.authors a
       join b.ratings r
       where ((:authors is null or a in :authors)
            or (:publishers is null or b.publisher in :publishers))
       and (:title is null or lower(b.title) like lower(concat('%', :title, '%')))
       group by b.id
       having AVG(r.bookRating) >= :desiredAverageRating
       """)
    Page<BookRatingProjection> findByAuthorsOrPublisherIn(@Param("authors") List<Author> authors,
                                                          @Param("publishers") List<Publisher> publishers,
                                                          @Param("desiredAverageRating") Integer desiredAverageRating,
                                                          @Param("title") String title,
                                                          Pageable pageable);
}