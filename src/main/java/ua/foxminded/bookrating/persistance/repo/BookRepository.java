package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;

import java.util.Optional;

@Repository
public interface BookRepository extends BaseRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByIsbn(String isbn);

    @Query("""
            select b.ratings
            from Book b
            join b.ratings r
            where r.book = :book""")
    Page<Rating> findBookRatings(@Param("book") Book book, Pageable pageable);

    @Query(value = "select * from Book b where b.deleted = true and b.isbn = :isbn", nativeQuery = true)
    Optional<Book> findDeletedBookByIsbn(@Param("isbn") String isbn);
}