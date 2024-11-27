package ua.foxminded.bookrating.util.author;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.util.book.BookData;

import java.util.List;

@Getter
public class AuthorsData {

    private final Long id = 4L;
    private final Long id2 = 2172L;
    private final String name = "Scott Turow";
    private final String name2 = "Kazuo Ishiguro";
    private final String updatedName = "Test Name";
    private final Author author = new Author(id, name);
    private final Author author2 = new Author(id2, name2);
    private final Author newAuthor = new Author(name);
    private final Author updatedAuthor = new Author(updatedName);

    private final Page<Author> authors = new PageImpl<>(
            List.of(author, author2),
            PageRequest.of(0, 2), 100
    );
}
