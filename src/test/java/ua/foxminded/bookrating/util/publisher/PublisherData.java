package ua.foxminded.bookrating.util.publisher;

import lombok.Getter;
import ua.foxminded.bookrating.persistance.entity.Publisher;

@Getter
public class PublisherData {
    private final Long id = 1577L;
    private final String name = "Books on Tape";
    private final String publisherTestName = "test name";
    private final Publisher publisher = new Publisher(id, name);

}
