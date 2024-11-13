package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


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
        assertThat(publisherRepository.getBooksByEntity(PUBLISHER_DATA.getPublisher(), 0, Pageable.ofSize(30)).getContent()).hasSize(10);
    }

    @Test
    void findByName() {
        assertThat(publisherRepository.findByName(PUBLISHER_DATA.getName())).isPresent();
    }

    @Test
    void findAllPaginated() {
        assertThat(publisherRepository.findAllPaginated(Pageable.ofSize(10)).getContent()).hasSize(10);
    }

    @Test
    void findByNameContainingIgnoreCase() {
        assertThat(publisherRepository.findByNameContainingIgnoreCase(PUBLISHER_DATA.getName(), Pageable.ofSize(10)).getContent()).hasSize(1);
    }

    @Test
    void findById() {
        assertThat(publisherRepository.findById(PUBLISHER_DATA.getId())).isPresent();
    }

    @Test
    void findAll() {
        assertThat(publisherRepository.findAll()).hasSize(10);
    }

    @Test
    void save() {
        int countOfPublishers = publisherRepository.findAll().size();
        assertAll(() -> {
            assertThat(publisherRepository.save(new Publisher(PUBLISHER_DATA.getPublisherTestName()))).isNotNull();
            assertThat(publisherRepository.findAll()).hasSize(countOfPublishers + 1);
        });
    }

    @Test
    void update() {
        Publisher publisher = publisherRepository.findById(PUBLISHER_DATA.getId()).get();
        publisher.setName(PUBLISHER_DATA.getPublisherTestName());
        publisherRepository.save(publisher);
        assertThat(publisherRepository.findById(PUBLISHER_DATA.getId()).get().getName()).isEqualTo(PUBLISHER_DATA.getPublisherTestName());
    }

    @Test
    void delete() {
        int size = publisherRepository.findAll().size();
        publisherRepository.delete(PUBLISHER_DATA.getPublisher());
        assertThat(publisherRepository.findAll()).hasSize(size - 1);
    }
}