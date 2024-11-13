package ua.foxminded.bookrating.util.book;

import lombok.Getter;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Image;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

@Getter
public class BookData extends AuthorsData {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    private final Long id = 110464L;
    private final String isbn = "0736688390";
    private final String title = "Reversible Errors";
    private final String publicationYear = "2003";
    private final Book book = getFullBook();
    private final Book simpleBook = getWithoutId();
    
    private Book getWithoutId() {
        Book b = new Book();
        b.setTitle(title);
        b.setIsbn(isbn);
        b.setPublicationYear(publicationYear);
        b.setImage(getImage());
        b.setPublisher(PUBLISHER_DATA.getPublisher());
        b.addAuthor(AUTHORS_DATA.getAuthor());
        return b;
    }

    private Book getFullBook() {
        Book b = getWithoutId();
        b.setId(id);
        return b;
    }

    private Image getImage() {
        Image image = new Image("http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
                "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
                "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg");
        image.setId(110464L);
        return image;
    }
}
