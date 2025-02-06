package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.specification.BookSpecification;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class BookRepositoryTest {

    public static final BookData BOOK_DATA = new BookData();
    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RatingRepository ratingRepository;

    @Test
    void findByIsbn() {
        assertAll(() -> {
            Optional<Book> result = bookRepository.findByIsbn(BOOK_DATA.getIsbn());
            assertTrue(result.isPresent());
            assertThat(result).contains(BOOK_DATA.getBook());
        });
    }

    @Test
    void findAll_shouldReturnPageOfBooks_whenIsInvoke() {
        assertAll(() -> {
            Page<Book> books = bookRepository.findAll(Pageable.unpaged());
            assertTrue(books.hasContent());
            assertThat(books.getTotalElements()).isEqualTo(38);
            assertThat(books.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findAll_shouldReturnListOfBooks_whenFilterAllParams() {
        Specification<Book> bookSpecification = Specification
                .where(BookSpecification.hasAuthors(List.of(AUTHORS_DATA.getAuthor())))
                .and(BookSpecification.hasPublishers(List.of(PUBLISHER_DATA.getPublisher())))
                .and(BookSpecification.hasTitle(BOOK_DATA.getTitle()))
                .and(BookSpecification.hasPublicationYear(Integer.parseInt(BOOK_DATA.getPublicationYear())))
                .and(BookSpecification.hasAverageRating(3));

        assertAll(() -> {
            Page<Book> books = bookRepository.findAll(bookSpecification, Pageable.unpaged());
            assertTrue(books.hasContent());
            assertThat(books.getTotalElements()).isEqualTo(1);
            assertThat(books.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findAll_shouldReturnListOfAuthorBooks_whenFilterByAuthor() {
        Specification<Book> bookSpecification = Specification.where(BookSpecification.hasAuthors(List.of(AUTHORS_DATA.getAuthor())));
        assertAll(() -> {
            Page<Book> books = bookRepository.findAll(bookSpecification, Pageable.unpaged());
            assertTrue(books.hasContent());
            assertThat(books.getTotalElements()).isEqualTo(24);
            assertThat(books.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findAll_shouldReturnListOfPublisherBooks_whenFilterByPublisher() {
        Specification<Book> bookSpecification = Specification.where(BookSpecification.hasPublishers(List.of(PUBLISHER_DATA.getPublisher())));
        assertAll(() -> {
            Page<Book> books = bookRepository.findAll(bookSpecification, Pageable.unpaged());
            assertTrue(books.hasContent());
            assertThat(books.getTotalElements()).isEqualTo(10);
            assertThat(books.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findAll_shouldReturnListOfBooksContainsTitle_whenFilterByTitle() {
        Specification<Book> bookSpecification = Specification.where(BookSpecification.hasTitle(BOOK_DATA.getTitle()));
        assertAll(() -> {
            Page<Book> books = bookRepository.findAll(bookSpecification, Pageable.unpaged());
            assertTrue(books.hasContent());
            assertThat(books.getTotalElements()).isEqualTo(3);
            assertThat(books.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findAll_shouldReturnListOfBooksWithPublishedYear_whenFilterByPublishedYear() {
        Specification<Book> bookSpecification = Specification.where(BookSpecification.hasPublicationYear(Integer.parseInt(BOOK_DATA.getPublicationYear())));
        assertAll(() -> {
            Page<Book> books = bookRepository.findAll(bookSpecification, Pageable.unpaged());
            assertTrue(books.hasContent());
            assertThat(books.getTotalElements()).isEqualTo(3);
            assertThat(books.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findAll_shouldReturnListOfBooksWithRatingGraterThan_whenFilterByRating() {
        Specification<Book> bookSpecification = Specification.where(BookSpecification.hasAverageRating(3));
        assertAll(() -> {
            Page<Book> books = bookRepository.findAll(bookSpecification, Pageable.unpaged());
            assertTrue(books.hasContent());
            assertThat(books.getTotalElements()).isEqualTo(15);
            assertThat(books.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    @Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql"})
    void save() {
        assertAll(() -> {
            assertThat(bookRepository.findAll()).isEmpty();
            assertThat(bookRepository.save(BOOK_DATA.getNewBook())).isNotNull();
            assertThat(bookRepository.findAll()).hasSize(1);
        });
    }

    @Test
    void delete_shouldDeleteBook_whenInvoke() {
        assertAll(() -> {
            assertThat(bookRepository.findAll()).hasSize(38);
            bookRepository.delete(BOOK_DATA.getBook());
            assertThat(bookRepository.findAll()).hasSize(37);
        });
    }

    @Test
    void delete_shouldDeleteBookRatings_whenBooksIsDeleted() {
        assertAll(() -> {
            int bookRatingsCount = bookRepository.findBookRatings(BOOK_DATA.getBook(), Pageable.unpaged()).getContent().size();
            int allRatingsCount = ratingRepository.findAll().size();

            assertThat(allRatingsCount).isEqualTo(1006);

            bookRepository.delete(BOOK_DATA.getBook());
            bookRepository.flush();

            assertThat(ratingRepository.findAll()).hasSize(allRatingsCount - bookRatingsCount);
        });
    }

    @Test
    void findAll() {
        assertThat(bookRepository.findAll()).hasSize(38);
    }

    @Test
    void findById() {
        assertAll(() -> {
            Optional<Book> result = bookRepository.findById(BOOK_DATA.getId());
            assertTrue(result.isPresent());
            assertThat(result).contains(BOOK_DATA.getBook());
        });
    }

    @Test
    void findBookRatings() {
        assertAll(() -> {
            Page<Rating> bookRatings = bookRepository.findBookRatings(BOOK_DATA.getBook(), Pageable.unpaged());
            assertTrue(bookRatings.hasContent());
            assertThat(bookRatings.getTotalElements()).isEqualTo(1);
            assertThat(bookRatings.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findDeletedBookByIsbn_shouldReturnDeletedBook_whenIsFound() {
        assertTrue(bookRepository.findDeletedBookByIsbn(BOOK_DATA.getDeletedBooksIsbn()).isPresent());
    }

    @Test
    void restore_shouldRestoreBook_whenIsFound() {
        assertAll(() -> {
            assertThat(bookRepository.findAll()).hasSize(38);
            bookRepository.restore(BOOK_DATA.getDeletedBookId());
            assertThat(bookRepository.findAll()).hasSize(39);
        });
    }

    @Test
    void restore_shouldRestoreBookRatings_whenBookIsRestored() {
        assertAll(() -> {
            assertThat(ratingRepository.findAll()).hasSize(1006);
            bookRepository.restore(BOOK_DATA.getDeletedBookId());
            bookRepository.flush();
            assertThat(bookRepository.findById(BOOK_DATA.getDeletedBookId()).get().getRatings()).hasSize(2);
            assertThat(ratingRepository.findAll()).hasSize(1008);
        });
    }

}