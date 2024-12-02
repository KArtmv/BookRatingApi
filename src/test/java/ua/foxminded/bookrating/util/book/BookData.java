package ua.foxminded.bookrating.util.book;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Image;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.time.Year;
import java.util.Collections;
import java.util.List;

@Getter
public class BookData {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    private final Long id = 110464L;
    private final String isbn = "0736688390";
    private final String title = "Reversible Errors";
    private final String publicationYear = "2003";

    private final String updatedIsbn = "0736688399";
    private final String updatedTitle = "Updated book title";
    private final String updatedPublicationYear = "2000";
    private final Image image = new Image("http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
            "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
            "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg");

    private final Book book = new Book(id, isbn, title, publicationYear, PUBLISHER_DATA.getPublisher(), Collections.singleton(AUTHORS_DATA.getAuthor()), image);
    private final Book newBook = new Book(isbn, title, publicationYear, PUBLISHER_DATA.getPublisher(), Collections.singleton(AUTHORS_DATA.getAuthor()), image);

    public BookDto bookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn(isbn);
        bookDto.setTitle(title);
        bookDto.setPublicationYear(Year.of(Integer.parseInt(publicationYear)));
        bookDto.setPublisherId(PUBLISHER_DATA.getId());
        bookDto.setAuthorsId(Collections.singletonList(AUTHORS_DATA.getId()));
        bookDto.setImage(image);
        return bookDto;
    }

    public BookDto bookDtoUpdate() {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setIsbn(updatedIsbn);
        bookDto.setTitle(updatedTitle);
        bookDto.setPublicationYear(Year.of(Integer.parseInt(updatedPublicationYear)));
        bookDto.setPublisherId(PUBLISHER_DATA.getId());
        bookDto.setAuthorsId(Collections.singletonList(AUTHORS_DATA.getId()));
        bookDto.setImage(image);
        return bookDto;
    }

    private final Page<BookRatingProjection> bookRatingProjections = new PageImpl<>(
            List.of(
                    new BookRatingProjection() {
                        @Override
                        public Book getBook() {
                            return book;
                        }

                        @Override
                        public Double getAverageRating() {
                            return 0.0;
                        }
                    }
            ),
            PageRequest.of(0, 10), 1
    );

    private final String selfHref = "http://localhost/api/v1/books/110464";


}
