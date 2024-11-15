package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class PublisherRepositoryTest {

    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void getBooksByPublisher() {
        assertAll(() -> {
            Page<BookRatingProjection> result = publisherRepository.getBooksByEntity(PUBLISHER_DATA.getPublisher(), 0, Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(10);
            assertThat(result.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByName() {
        assertAll(() -> {
            Optional<Publisher> result = publisherRepository.findByName(PUBLISHER_DATA.getName());
            assertTrue(result.isPresent());
            assertThat(result).contains(PUBLISHER_DATA.getPublisher());
        });
    }

    @Test
    void findAllPaginated() {
        assertAll(() -> {
            Page<Publisher> result = publisherRepository.findAllPaginated(Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(10);
            assertThat(result.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByNameContainingIgnoreCase() {
        assertAll(() -> {
            Page<Publisher> result = publisherRepository.findByNameContainingIgnoreCase(PUBLISHER_DATA.getName(), Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findById() {
        assertAll(() -> {
            Optional<Publisher> result = publisherRepository.findById(PUBLISHER_DATA.getId());
            assertTrue(result.isPresent());
            assertThat(result).contains(PUBLISHER_DATA.getPublisher());
        });
    }

    @Test
    void findAll() {
        assertThat(publisherRepository.findAll()).hasSize(10);
    }

    @Test
    @Sql(scripts = {"/sql/authors.sql"})
    void save() {
        assertAll(() -> {
            assertThat(publisherRepository.findAll()).isEmpty();
            assertThat(publisherRepository.save(PUBLISHER_DATA.getNewPublisher())).isNotNull();
            assertThat(publisherRepository.findAll()).hasSize(1);
        });
    }

    @Test
    void delete() {
        assertAll(() -> {
            assertThat(publisherRepository.findAll()).hasSize(10);
            publisherRepository.delete(PUBLISHER_DATA.getPublisher());
            assertThat(publisherRepository.findAll()).hasSize(9);
        });
    }
}