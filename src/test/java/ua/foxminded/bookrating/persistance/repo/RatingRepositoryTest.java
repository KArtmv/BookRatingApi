package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.rating.RatingData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class RatingRepositoryTest {

    public static final RatingData RATING_DATA = new RatingData();
    public static final BookData BOOK_DATA = new BookData();

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    void getRatingsByBook() {
        assertAll(() -> {
            Page<Rating> result = ratingRepository.getRatingsByBook(BOOK_DATA.getBook(), Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    @Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql"})
    void save() {
        assertAll(() -> {
            assertThat(ratingRepository.findAll()).isEmpty();
            assertThat(ratingRepository.save(RATING_DATA.getNewRating())).isNotNull();
            assertThat(ratingRepository.findAll()).hasSize(1);
        });
    }

    @Test
    void delete() {
        assertAll(() -> {
            assertThat(ratingRepository.findAll()).hasSize(1006);
            ratingRepository.delete(RATING_DATA.getRating());
            assertThat(ratingRepository.findAll()).hasSize(1005);
        });
    }

    @Test
    void findById() {
        assertAll(() -> {
            Optional<Rating> result = ratingRepository.findById(RATING_DATA.getId());
            assertTrue(result.isPresent());
            assertThat(result).contains(RATING_DATA.getRating());
        });
    }
}