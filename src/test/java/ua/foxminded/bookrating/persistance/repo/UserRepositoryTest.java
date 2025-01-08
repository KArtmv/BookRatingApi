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
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.util.user.UserData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class UserRepositoryTest {

    public static final UserData USER_DATA = new UserData();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;

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
    @Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/books.sql", "/sql/book-authors.sql"})
    void save() {
        assertAll(() -> {
            assertThat(userRepository.findAll()).isEmpty();
            assertThat(userRepository.save(USER_DATA.getNewUser())).isNotNull();
            assertThat(userRepository.findAll()).hasSize(1);
        });
    }

    @Test
    void delete_shouldDeleteUser_whenInvoke() {
        assertAll(() -> {
            assertThat(userRepository.findAll()).hasSize(774);
            userRepository.deleteById(USER_DATA.getId());
            assertThat(userRepository.findAll()).hasSize(773);
        });
    }

    @Test
    void delete_shouldDeleteUserRatings_whenUserIsDeleted() {
        assertAll(() -> {
            int userRatingsCount = userRepository.findByRatingsUser(USER_DATA.getUser(), Pageable.unpaged()).getContent().size();
            int allRatingsCount = ratingRepository.findAll().size();

            assertThat(allRatingsCount).isEqualTo(1006);

            userRepository.delete(USER_DATA.getUser());
            userRepository.flush();

            assertThat(ratingRepository.findAll()).hasSize(allRatingsCount - userRatingsCount);
        });
    }

    @Test
    void restore_shouldRestoreDeletedUser_whenIsInvoked() {
        assertAll(() -> {
            assertThat(userRepository.findAll()).hasSize(774);
            userRepository.restore(USER_DATA.getDeletedUserId());
            userRepository.flush();
            assertThat(userRepository.findAll()).hasSize(775);
        });
    }

    @Test
    void restore_shouldRestoreUserRatings_whenUserIsRestored() {
        assertAll(() -> {
            assertThat(ratingRepository.findAll()).hasSize(1006);
            userRepository.restore(USER_DATA.getDeletedUserId());
            userRepository.flush();
            assertThat(userRepository.findById(USER_DATA.getDeletedUserId()).get().getRatings()).hasSize(5);
            assertThat(ratingRepository.findAll()).hasSize(1011);
        });
    }
}