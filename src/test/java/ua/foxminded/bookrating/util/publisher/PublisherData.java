package ua.foxminded.bookrating.util.publisher;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.foxminded.bookrating.dto.PublisherDto;
import ua.foxminded.bookrating.persistance.entity.Publisher;

import java.util.List;

@Getter
public class PublisherData {

    private final Long id = 1577L;
    private final Long id2 = 21L;
    private final String name = "Books on Tape";
    private final String name2 = "Goldmann";
    private final String updatedName = "test name";
    private final Publisher publisher = new Publisher(id, name);
    private final Publisher publisher2 = new Publisher(id2, name2);
    private final Publisher newPublisher = new Publisher(name);
    private final Publisher updatedPublisher = new Publisher(updatedName);
    private final Long deletedPublisherId = 1182L;
    private final String deletedPublisherName = "Simon and Schuster";
    private final PublisherDto publisherDto = new PublisherDto(name);
    private final PublisherDto updatedPublisherDto = new PublisherDto(updatedName);

    private final Page<Publisher> publishers = new PageImpl<>(
            List.of(publisher, publisher2),
            PageRequest.of(0, 2), 100
    );

    private final String selfHref = "http://localhost/api/v1/publishers/1577";
    private final String publisherBooksHref = "http://localhost/api/v1/publishers/1577/books";
}
