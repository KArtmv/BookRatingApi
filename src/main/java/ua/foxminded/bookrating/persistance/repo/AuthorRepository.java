package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.projection.BookRatingProjection;

@Repository
public interface AuthorRepository extends BaseRepository<Author, Long> {

    @Query("""
            SELECT b as book, AVG(r.bookRating) AS averageRating
            FROM Book b
            JOIN b.authors a
            JOIN b.ratings r
            WHERE a = :author
            GROUP BY b
            HAVING AVG(r.bookRating) > :desiredAverageRating
            """)
    Page<BookRatingProjection> getBooksByAuthor(@Param("author") Author author,
                                                @Param("desiredAverageRating") Integer desiredAverageRating,
                                                Pageable pageable);
}