package ua.foxminded.bookrating.util.book;

import lombok.Getter;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.image.ImageData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.Collections;

@Getter
public class BookData {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();
    public static final ImageData IMAGE_DATA = new ImageData();

    private final Long id = 110464L;
    private final String isbn = "0736688390";
    private final String title = "Reversible Errors";
    private final String publicationYear = "2003";

    private final Book book = new Book(id, isbn, title, publicationYear, PUBLISHER_DATA.getPublisher(), Collections.singleton(AUTHORS_DATA.getAuthor()), IMAGE_DATA.getImage());
    private final Book newBook = new Book(isbn, title, publicationYear, PUBLISHER_DATA.getPublisher(), Collections.singleton(AUTHORS_DATA.getAuthor()), IMAGE_DATA.getImage());
}
