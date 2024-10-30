package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> getRatingsByBook(Book book, Pageable pageable);
}