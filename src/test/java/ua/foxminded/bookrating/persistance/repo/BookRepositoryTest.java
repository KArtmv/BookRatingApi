package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class BookRepositoryTest {

    public static final BookData BOOK_DATA = new BookData();
    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByIsbn() {
        assertAll(() -> {
            Optional<Book> result = bookRepository.findByIsbn(BOOK_DATA.getIsbn());
            assertTrue(result.isPresent());
            assertThat(result.get()).isEqualTo(BOOK_DATA.getBook());
        });
    }

    @Test
    void findAllPaginated() {
        assertAll(() -> {
            Page<BookRatingProjection> allPaginated = bookRepository.findAllPaginated(0, Pageable.unpaged());
            assertTrue(allPaginated.hasContent());
            assertThat(allPaginated.getTotalElements()).isEqualTo(33);
            assertThat(allPaginated.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByTitleContainingIgnoreCase() {
        assertAll(() -> {
            Page<BookRatingProjection> byTitleContainingIgnoreCase = bookRepository.findByTitleContainingIgnoreCase(BOOK_DATA.getTitle(), Pageable.unpaged());
            assertTrue(byTitleContainingIgnoreCase.hasContent());
            assertThat(byTitleContainingIgnoreCase.getTotalElements()).isEqualTo(3);
            assertThat(byTitleContainingIgnoreCase.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByAuthorsOrPublisherIn_whenAuthor() {
        assertAll(() -> {
            Page<BookRatingProjection> byAuthorsOrPublisherIn = bookRepository.findByAuthorsOrPublisherIn(Collections.singletonList(AUTHORS_DATA.getAuthor()),
                    Collections.emptyList(), 0, "", Pageable.unpaged());
            assertTrue(byAuthorsOrPublisherIn.hasContent());
            assertThat(byAuthorsOrPublisherIn.getTotalElements()).isEqualTo(24);
            assertThat(byAuthorsOrPublisherIn.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByAuthorsOrPublisherIn_whenPublisher() {
        assertAll(() -> {
            Page<BookRatingProjection> byAuthorsOrPublisherIn = bookRepository.findByAuthorsOrPublisherIn(Collections.emptyList(),
                    Collections.singletonList(PUBLISHER_DATA.getPublisher()), 0, "", Pageable.unpaged());
            assertTrue(byAuthorsOrPublisherIn.hasContent());
            assertThat(byAuthorsOrPublisherIn.getTotalElements()).isEqualTo(10);
            assertThat(byAuthorsOrPublisherIn.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByAuthorsOrPublisherIn_whenAuthorAndPublisher() {
        assertAll(() -> {
            Page<BookRatingProjection> byAuthorsOrPublisherIn = bookRepository.findByAuthorsOrPublisherIn(
                    Collections.singletonList(AUTHORS_DATA.getAuthor()),
                    Collections.singletonList(PUBLISHER_DATA.getPublisher()), 0, "", Pageable.unpaged());
            assertTrue(byAuthorsOrPublisherIn.hasContent());
            assertThat(byAuthorsOrPublisherIn.getTotalElements()).isEqualTo(33);
            assertThat(byAuthorsOrPublisherIn.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findByAuthorsOrPublisherIn_whenAuthorAndPublisherAndTitle() {
        assertAll(() -> {
            Page<BookRatingProjection> byAuthorsOrPublisherIn = bookRepository.findByAuthorsOrPublisherIn(
                    Collections.singletonList(AUTHORS_DATA.getAuthor()),
                    Collections.singletonList(PUBLISHER_DATA.getPublisher()),
                    0, BOOK_DATA.getTitle(), Pageable.unpaged());
            assertTrue(byAuthorsOrPublisherIn.hasContent());
            assertThat(byAuthorsOrPublisherIn.getTotalElements()).isEqualTo(3);
            assertThat(byAuthorsOrPublisherIn.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    @Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql"})
    void save() {
        assertAll(() -> {
            int countOfEntries = bookRepository.findAll().size();
            assertThat(countOfEntries).isZero();
            assertThat(bookRepository.save(BOOK_DATA.getSimpleBook())).isNotNull();
            assertThat(bookRepository.findAll()).hasSize(countOfEntries + 1);
        });
    }

    @Test
    void delete() {
        assertAll(() -> {
            int countOfEntries = bookRepository.findAll().size();
            assertThat(countOfEntries).isEqualTo(33);
            bookRepository.delete(BOOK_DATA.getBook());
            assertThat(bookRepository.findAll()).hasSize(countOfEntries - 1);
        });
    }

    @Test
    void findAll() {
        assertThat(bookRepository.findAll()).hasSize(33);
    }
}