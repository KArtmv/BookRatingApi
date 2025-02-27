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
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class PublisherRepositoryTest {

    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RatingRepository ratingRepository;

    @Test
    void getBooksByPublisher() {
        assertAll(() -> {
            Page<Book> result = publisherRepository.getBooksByEntity(PUBLISHER_DATA.getPublisher(), Pageable.unpaged());
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
            Page<Publisher> result = publisherRepository.findAll(Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(23);
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
        assertThat(publisherRepository.findAll()).hasSize(23);
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
            List<Book> publisherBooks = publisherRepository.getBooksByEntity(PUBLISHER_DATA.getPublisher(), Pageable.unpaged()).getContent();
            int publisherBooksCount = publisherBooks.size();
            int allBooksCount = bookRepository.findAll().size();
            int allRatingsCount = ratingRepository.findAll().size();
            int publisherBooksRatingsCount = publisherBooks.stream().mapToInt(book -> book.getRatings().size()).sum();

            assertThat(allBooksCount).isEqualTo(38);
            assertThat(allRatingsCount).isEqualTo(1006);
            assertThat(publisherBooksCount).isEqualTo(10);

            assertThat(publisherRepository.findAll()).hasSize(23);
            publisherRepository.delete(PUBLISHER_DATA.getPublisher());
            assertThat(publisherRepository.findAll()).hasSize(22);

            assertThat(bookRepository.findAll()).hasSize(allBooksCount - publisherBooksCount);
            assertThat(ratingRepository.findAll()).hasSize(allRatingsCount - publisherBooksRatingsCount);
        });
    }


    @Test
    void delete_shouldDeletePublisher_whenIsInvoke() {
        assertAll(() -> {
            assertThat(publisherRepository.findAll()).hasSize(23);
            publisherRepository.delete(PUBLISHER_DATA.getPublisher());
            assertThat(publisherRepository.findAll()).hasSize(22);

            assertTrue(publisherRepository.findDeletedByName(PUBLISHER_DATA.getDeletedPublisherName()).isPresent());
        });
    }

    @Test
    void delete_shouldDeletePublisherBooks_whenPublisherIsDeleted() {
        assertAll(() -> {
            int authorBooksCount = publisherRepository.getBooksByEntity(PUBLISHER_DATA.getPublisher(), Pageable.unpaged()).getContent().size();
            int allBooksCount = bookRepository.findAll().size();

            assertThat(authorBooksCount).isEqualTo(10);
            assertThat(allBooksCount).isEqualTo(38);

            publisherRepository.delete(PUBLISHER_DATA.getPublisher());
            publisherRepository.flush();

            List<Book> allBooksAfterPublisherDeleting = bookRepository.findAll();
            assertThat(allBooksAfterPublisherDeleting).hasSize(allBooksCount - authorBooksCount);
        });
    }

    @Test
    void delete_shouldDeleteRatingsOfPublisherBooks_whenPublisherIsDeleted() {
        assertAll(() -> {
            int allRatingsCount = ratingRepository.findAll().size();
            int authorBooksRatingsCount = publisherRepository.getBooksByEntity(PUBLISHER_DATA.getPublisher(), Pageable.unpaged()).getContent()
                    .stream().mapToInt(book -> book.getRatings().size()).sum();

            assertThat(allRatingsCount).isEqualTo(1006);
            publisherRepository.delete(PUBLISHER_DATA.getPublisher());
            publisherRepository.flush();

            List<Rating> allRatingsAfterPublisherDeleting = ratingRepository.findAll();
            assertThat(allRatingsAfterPublisherDeleting).hasSize(allRatingsCount - authorBooksRatingsCount);
        });
    }

    @Test
    void findDeletedEntityByName_shouldReturnAppropriatePublisher_whenIsFound() {
        assertTrue(publisherRepository.findDeletedByName(PUBLISHER_DATA.getDeletedPublisherName()).isPresent());
    }

    @Test
    void restore_shouldRestorePublisher_whenInvoke() {
        assertAll(() -> {
            assertFalse(publisherRepository.findByName(PUBLISHER_DATA.getDeletedPublisherName()).isPresent());
            publisherRepository.restore(PUBLISHER_DATA.getDeletedPublisherId());
            assertTrue(publisherRepository.findByName(PUBLISHER_DATA.getDeletedPublisherName()).isPresent());
        });
    }

    @Test
    void restore_shouldRestorePublisherBooks_whenRestorePublisher() {
        assertAll(() -> {
            int countOfAllBooks = bookRepository.findAll().size();
            assertThat(countOfAllBooks).isEqualTo(38);
            publisherRepository.restore(PUBLISHER_DATA.getDeletedPublisherId());
            int countOfPublisherBooks = publisherRepository.findByName(PUBLISHER_DATA.getDeletedPublisherName()).get().getBooks().size();
            assertThat(countOfPublisherBooks).isEqualTo(6);
            assertThat(bookRepository.findAll()).hasSize(countOfAllBooks + countOfPublisherBooks);
        });
    }

    @Test
    void restore_shouldRestorePublisherBooksRatings_whenRestorePublisher() {
        assertAll(() -> {
            int countOfAllRatings = ratingRepository.findAll().size();
            assertThat(countOfAllRatings).isEqualTo(1006);
            publisherRepository.restore(PUBLISHER_DATA.getDeletedPublisherId());
            int countOfPublisherBooksRatings = publisherRepository.findById(PUBLISHER_DATA.getDeletedPublisherId()).get().getBooks().stream().map(Book::getRatings).map(Set::size).mapToInt(Integer::intValue).sum();
            assertThat(countOfPublisherBooksRatings).isEqualTo(11);
            assertThat(ratingRepository.findAll()).hasSize(countOfAllRatings + countOfPublisherBooksRatings);
        });
    }
}