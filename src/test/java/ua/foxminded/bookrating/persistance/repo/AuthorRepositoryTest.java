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
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.util.author.AuthorsData;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class AuthorRepositoryTest {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RatingRepository ratingRepository;

    @Test
    void getBooksByAuthor() {
        assertAll(() -> {
            Page<Book> result = authorRepository.getBooksByEntity(AUTHORS_DATA.getAuthor(), Pageable.unpaged());
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
            Page<Author> result = authorRepository.findAll(Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(24);
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
        assertThat(authorRepository.findAll()).hasSize(24);
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
    void delete_shouldDeleteAuthor_whenIsInvoke() {
        assertAll(() -> {
            assertThat(authorRepository.findAll()).hasSize(24);
            authorRepository.delete(AUTHORS_DATA.getAuthor());
            assertThat(authorRepository.findAll()).hasSize(23);

            assertTrue(authorRepository.findDeletedByName(AUTHORS_DATA.getDeletedAuthorName()).isPresent());
        });
    }

    @Test
    void delete_shouldDeleteAuthorBooks_whenAuthorIsDeleted() {
        assertAll(() -> {
            int authorBooksCount = authorRepository.getBooksByEntity(AUTHORS_DATA.getAuthor(), Pageable.unpaged()).getContent().size();
            int allBooksCount = bookRepository.findAll().size();

            assertThat(authorBooksCount).isEqualTo(24);
            assertThat(allBooksCount).isEqualTo(38);

            authorRepository.delete(AUTHORS_DATA.getAuthor());
            authorRepository.flush();

            List<Book> allBooksAfterAuthorDeleting = bookRepository.findAll();
            assertThat(allBooksAfterAuthorDeleting).hasSize(allBooksCount - authorBooksCount);
        });
    }

    @Test
    void delete_shouldDeleteRatingsOfAuthorBooks_whenAuthorIsDeleted() {
        assertAll(() -> {
            int allRatingsCount = ratingRepository.findAll().size();
            int authorBooksRatingsCount = authorRepository.getBooksByEntity(AUTHORS_DATA.getAuthor(), Pageable.unpaged()).getContent()
                    .stream().mapToInt(book -> book.getRatings().size()).sum();

            assertThat(allRatingsCount).isEqualTo(1006);
            authorRepository.delete(AUTHORS_DATA.getAuthor());
            authorRepository.flush();

            List<Rating> allRatingsAfterAuthorDeleting = ratingRepository.findAll();
            assertThat(allRatingsAfterAuthorDeleting).hasSize(allRatingsCount - authorBooksRatingsCount);
        });
    }

    @Test
    void findDeletedEntityByName_shouldReturnAppropriateAuthor_whenIsFound() {
        assertTrue(authorRepository.findDeletedByName(AUTHORS_DATA.getDeletedAuthorName()).isPresent());
    }

    @Test
    void restore_shouldRestoreAuthor_whenInvoke() {
        assertAll(() -> {
            assertFalse(authorRepository.findByName(AUTHORS_DATA.getDeletedAuthorName()).isPresent());
            authorRepository.restore(AUTHORS_DATA.getDeletedAuthorId());
            assertTrue(authorRepository.findByName(AUTHORS_DATA.getDeletedAuthorName()).isPresent());
        });
    }

    @Test
    void restore_shouldRestoreAuthorBooks_whenRestoreAuthor() {
        assertAll(() -> {
            int countOfAllBooks = bookRepository.findAll().size();
            assertThat(countOfAllBooks).isEqualTo(38);
            authorRepository.restore(AUTHORS_DATA.getDeletedAuthorId());
            int countOfAuthorBooks = authorRepository.findByName(AUTHORS_DATA.getDeletedAuthorName()).get().getBooks().size();
            assertThat(countOfAuthorBooks).isEqualTo(4);
            assertThat(bookRepository.findAll()).hasSize(countOfAllBooks + countOfAuthorBooks);
        });
    }

    @Test
    void restore_shouldRestoreAuthorBooksRatings_whenRestoreAuthor() {
        assertAll(() -> {
            int countOfAllRatings = ratingRepository.findAll().size();
            assertThat(countOfAllRatings).isEqualTo(1006);
            authorRepository.restore(AUTHORS_DATA.getDeletedAuthorId());
            int countOfAuthorBooksRatings = authorRepository.findById(AUTHORS_DATA.getDeletedAuthorId()).get().getBooks().stream().map(Book::getRatings).map(Set::size).mapToInt(Integer::intValue).sum();
            assertThat(countOfAuthorBooksRatings).isEqualTo(12);
            assertThat(ratingRepository.findAll()).hasSize(countOfAllRatings + countOfAuthorBooksRatings);
        });
    }
}