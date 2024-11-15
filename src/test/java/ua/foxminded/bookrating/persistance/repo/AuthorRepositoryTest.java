package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.util.author.AuthorsData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class AuthorRepositoryTest {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void getBooksByAuthor() {
        assertAll(() -> {
            Page<BookRatingProjection> result = authorRepository.getBooksByEntity(AUTHORS_DATA.getAuthor(), 0, Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(24);
            assertThat(result.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByName() {
        assertAll(() -> {
            Optional<Author> result = authorRepository.findByName(AUTHORS_DATA.getName());
            assertTrue(result.isPresent());
            assertThat(result).contains(AUTHORS_DATA.getAuthor());
        });
    }

    @Test
    void findAllPaginated() {
        assertAll(() -> {
            Page<Author> result = authorRepository.findAllPaginated(Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(10);
            assertThat(result.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByNameContainingIgnoreCase() {
        assertAll(() -> {
            Page<Author> result = authorRepository.findByNameContainingIgnoreCase(AUTHORS_DATA.getName(), Pageable.ofSize(10));
            assertTrue(result.hasContent());
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent()).hasSize(1);
        });
    }

    @Test
    void findById() {
        assertAll(() -> {
            Optional<Author> result = authorRepository.findById(AUTHORS_DATA.getId());
            assertTrue(result.isPresent());
            assertThat(result).contains(AUTHORS_DATA.getAuthor());
        });
    }

    @Test
    void findAll() {
        assertThat(authorRepository.findAll()).hasSize(10);
    }

    @Test
    @Sql(scripts = {"/sql/publishers.sql"})
    void save() {
        assertAll(() -> {
            assertThat(authorRepository.findAll()).isEmpty();
            authorRepository.save(AUTHORS_DATA.getNewAuthor());
            assertThat(authorRepository.findAll()).hasSize(1);
        });
    }

    @Test
    void delete() {
        assertAll(() -> {
            assertThat(authorRepository.findAll()).hasSize(10);
            authorRepository.delete(AUTHORS_DATA.getAuthor());
            assertThat(authorRepository.findAll()).hasSize(9);
        });
    }
}