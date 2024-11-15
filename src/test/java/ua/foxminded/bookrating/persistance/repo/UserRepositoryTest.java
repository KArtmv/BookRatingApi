package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.util.user.UserData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class UserRepositoryTest {

    public static final UserData USER_DATA = new UserData();

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByRatingsUser() {
        assertAll(() -> {
            Page<Rating> result = userRepository.findByRatingsUser(USER_DATA.getUser(), Pageable.unpaged());
            assertTrue(result.hasContent());
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
        });
    }

    @Test
    void findById() {
        assertAll(() -> {
            Optional<User> result = userRepository.findById(USER_DATA.getId());
            assertTrue(result.isPresent());
            assertThat(result).contains(USER_DATA.getUser());
        });
    }

    @Test
    @Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql"})
    void save() {
        assertAll(() -> {
            assertThat(userRepository.findAll()).isEmpty();
            assertThat(userRepository.save(USER_DATA.getNewUser())).isNotNull();
            assertThat(userRepository.findAll()).hasSize(1);
        });
    }

    @Test
    void delete() {
        assertAll(() -> {
            assertThat(userRepository.findAll()).hasSize(744);
            userRepository.deleteById(USER_DATA.getId());
            assertThat(userRepository.findAll()).hasSize(743);
        });
    }
}