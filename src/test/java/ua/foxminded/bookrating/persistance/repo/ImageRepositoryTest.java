package ua.foxminded.bookrating.persistance.repo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.bookrating.util.image.ImageData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
//@Sql(scripts = {"/sql/authors.sql", "/sql/publishers.sql", "/sql/images.sql", "/sql/books.sql", "/sql/book-authors.sql", "/sql/users.sql", "/sql/ratings.sql"})
class ImageRepositoryTest {

    public static final ImageData IMAGE_DATA = new ImageData();

    @Autowired
    private ImageRepository imageRepository;

    @Test
    void save() {
        assertAll(() -> {
            assertThat(imageRepository.findAll()).isEmpty();
            assertThat(imageRepository.save(IMAGE_DATA.getNewImage())).isNotNull();
            assertThat(imageRepository.findAll()).hasSize(1);
        });
    }
}