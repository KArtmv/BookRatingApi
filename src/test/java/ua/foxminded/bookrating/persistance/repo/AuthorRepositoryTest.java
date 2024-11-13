package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.util.author.AuthorsData;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(authorRepository.getBooksByEntity(AUTHORS_DATA.getAuthor(), 0, Pageable.ofSize(30)).getContent()).hasSize(24);
    }

    @Test
    void findByName() {
        assertThat(authorRepository.findByName(AUTHORS_DATA.getName())).isPresent();
    }

    @Test
    void findAllPaginated() {
        assertThat(authorRepository.findAllPaginated(Pageable.ofSize(10)).getContent()).hasSize(10);
    }

    @Test
    void findByNameContainingIgnoreCase() {
        assertThat(authorRepository.findByNameContainingIgnoreCase(AUTHORS_DATA.getName(), Pageable.ofSize(10)).getContent()).hasSize(1);
    }

    @Test
    void findById() {
        assertThat(authorRepository.findById(AUTHORS_DATA.getId())).isPresent();
    }

    @Test
    void findAll() {
        assertThat(authorRepository.findAll()).hasSize(10);
    }

    @Test
    void save() {
        int countOfAuthors = authorRepository.findAll().size();
        authorRepository.save(new Author(AUTHORS_DATA.getAuthorTestName()));
        assertThat(authorRepository.findAll()).hasSize(countOfAuthors + 1);
    }

    @Test
    void update() {
        Author author = authorRepository.findById(AUTHORS_DATA.getId()).get();
        author.setName(AUTHORS_DATA.getAuthorTestName());
        authorRepository.save(author);
        assertThat(authorRepository.findById(AUTHORS_DATA.getId()).get().getName()).isEqualTo(AUTHORS_DATA.getAuthorTestName());
    }

    @Test
    void delete() {
        int size = authorRepository.findAll().size();
        authorRepository.delete(AUTHORS_DATA.getAuthor());
        assertThat(authorRepository.findAll()).hasSize(size - 1);
    }
}