package ua.foxminded.bookrating.util.author;

import lombok.Getter;
import ua.foxminded.bookrating.persistance.entity.Author;

@Getter
public class AuthorsData {

    private final Long id = 4L;
    private final String name = "Scott Turow";
    private final String updatedName = "Test Name";
    private final Author author = new Author(id, name);
    private final Author newAuthor = new Author(name);
    private final Author updatedAuthor = new Author(updatedName);
}
