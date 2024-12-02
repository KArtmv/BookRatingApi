package ua.foxminded.bookrating.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.bookrating.ValidatorConfig;
import ua.foxminded.bookrating.annotation.IsbnValidator;
import ua.foxminded.bookrating.assembler.*;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.persistance.repo.BookRepository;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;
import ua.foxminded.bookrating.util.rating.RatingData;
import ua.foxminded.bookrating.util.user.UserData;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Import({AuthorModelAssembler.class, PublisherModelAssembler.class, IsbnValidator.class,
        BookModelAssembler.class, SimpleBookModelAssembler.class, FullBookModelAssembler.class, ValidatorConfig.class,
        RatingModelAssembler.class, RatingModelAssembler.class, UserModelAssembler.class})
@WebMvcTest(BookController.class)
class BookControllerTest {

    public static final BookData BOOK_DATA = new BookData();
    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();
    public static final RatingData RATING_DATA = new RatingData();
    public static final UserData USER_DATA = new UserData();

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookService bookService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getBookRatings() throws Exception {
        when(bookService.getRatingsByBookId(anyLong(), any(Pageable.class))).thenReturn(RATING_DATA.getRatings());

        mockMvc.perform(get("/api/v1/books/{id}/ratings", BOOK_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.ratingModelList[0].user.id").value(USER_DATA.getId()),
                        jsonPath("$._embedded.ratingModelList[0].user.location").value(USER_DATA.getLocation()),
                        jsonPath("$._embedded.ratingModelList[0].user.age").value(USER_DATA.getAge()),
                        jsonPath("$._embedded.ratingModelList[0].user._links.ratedBooks.href").value(USER_DATA.getUserRatedBooksHref()),
                        jsonPath("$._embedded.ratingModelList[0].user._links.self.href").value(USER_DATA.getSelfHref()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/books/110464/ratings?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getBooks() throws Exception {
        when(bookService.findAllPaginated(anyInt(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookRatingProjections());

        mockMvc.perform(get("/api/v1/books")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value(BOOK_DATA.getPublicationYear()),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(BOOK_DATA.getBook());

        mockMvc.perform(get("/api/v1/books/{id}", BOOK_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(BOOK_DATA.getId()),
                        jsonPath("$.isbn").value(BOOK_DATA.getIsbn()),
                        jsonPath("$.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$.publicationYear").value(BOOK_DATA.getPublicationYear()),
                        jsonPath("$.averageRating").value("0.0"),
                        jsonPath("$.authors[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.authors[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$.authors[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$.authors[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$.publisher.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.publisher.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$.publisher._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$.publisher._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._links.bookRatings.href").value(BOOK_DATA.getBookRatingHref())
                );
    }

    @Test
    void getBookByIsbn() throws Exception {
        when(bookService.getByIsbn(anyString())).thenReturn(BOOK_DATA.getBook());

        mockMvc.perform(get("/api/v1/books/isbn").param("isbn", BOOK_DATA.getIsbn())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(BOOK_DATA.getId()),
                        jsonPath("$.isbn").value(BOOK_DATA.getIsbn()),
                        jsonPath("$.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$.publicationYear").value(BOOK_DATA.getPublicationYear()),
                        jsonPath("$.averageRating").value("0.0"),
                        jsonPath("$.authors[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.authors[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$.authors[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$.authors[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$.publisher.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.publisher.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$.publisher._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$.publisher._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._links.bookRatings.href").value(BOOK_DATA.getBookRatingHref())
                );
    }

    @Test
    void getBooksContainTitle() throws Exception {
        when(bookService.getByTitleContaining(anyString(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookRatingProjections());

        mockMvc.perform(get("/api/v1/books/title").param("title", "errors")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value(BOOK_DATA.getPublicationYear()),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void add_shouldReturnBookInstance_whenSuccessfullySaved() throws Exception {
        when(bookService.save(any(BookDto.class))).thenReturn(BOOK_DATA.getBook());
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/books").contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"isbn": "0195153448",
                             "title": "Classical Mythology",
                             "publicationYear": "2002",
                             "authorsId": [4],
                             "publisherId": 1577,
                             "image": {
                                 "imageUrlSmall": "http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
                                 "imageUrlMedium": "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
                                 "imageUrlLarge": "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"
                             }
                            }""")).andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(BOOK_DATA.getId()),
                        jsonPath("$.isbn").value(BOOK_DATA.getIsbn()),
                        jsonPath("$.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$.publicationYear").value(BOOK_DATA.getPublicationYear()),
                        jsonPath("$.averageRating").value("0.0"),
                        jsonPath("$.authors[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.authors[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$.authors[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$.authors[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$.publisher.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.publisher.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$.publisher._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$.publisher._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._links.bookRatings.href").value(BOOK_DATA.getBookRatingHref())
        );
    }

    @Test
    void add_shouldReturnErrorMessage_whenDataIsInvalid() throws Exception {
        when(bookService.save(any(BookDto.class))).thenReturn(BOOK_DATA.getBook());
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/books").contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"isbn": "1212121212",
                             "title": " ",
                             "publicationYear": "",
                             "authorsId": [],
                             "publisherId": "",
                             "image": {
                                 "imageUrlSmall": "",
                                 "imageUrlMedium": " ",
                                 "imageUrlLarge": "ftp://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"
                             }
                            }""")).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.isbn").value("The provided ISBN is not valid."),
                        jsonPath("$.title").value("The title of the book is required and cannot be empty."),
                        jsonPath("$.publicationYear").value("The publication year is required and cannot be null."),
                        jsonPath("$.authorsId").value("At least one author is required for the book."),
                        jsonPath("$.publisherId").value("The publisher is required and cannot be null."),
                        jsonPath("$.['image.imageUrlSmall']").value("Image URL Small is required"),
                        jsonPath("$.['image.imageUrlMedium']").value("Image URL Medium is required"),
                        jsonPath("$.['image.imageUrlLarge']").value("Image URL Large must be a valid HTTP URL.")
        );
    }

    @Test
    void add_shouldReturnErrorMessage_whenIsbnAlreadyExist() throws Exception {
        when(bookService.save(any(BookDto.class))).thenReturn(BOOK_DATA.getBook());
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(BOOK_DATA.getBook()));

        mockMvc.perform(post("/api/v1/books").contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"isbn": "0736688390",
                             "title": "Reversible Errors",
                             "publicationYear": "2003",
                             "authorsId": [4],
                             "publisherId": 1577,
                             "image": {
                                 "imageUrlSmall": "http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
                                 "imageUrlMedium": "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
                                 "imageUrlLarge": "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"
                             }
                            }""")).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.isbn").value("The provided ISBN already exists for another book.")
        );
    }

    @Test
    void update() throws Exception {
        when(bookService.update(anyLong(), any(BookDto.class))).thenReturn(BOOK_DATA.getBook());
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/books/{id}", BOOK_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"id": "110464",
                             "isbn": "0736688390",
                             "title": "Reversible Errors",
                             "publicationYear": "2003",
                             "authorsId": [4],
                             "publisherId": 1577,
                             "image": {
                                 "imageUrlSmall": "http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
                                 "imageUrlMedium": "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
                                 "imageUrlLarge": "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"
                             }
                            }""")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(BOOK_DATA.getId()),
                        jsonPath("$.isbn").value(BOOK_DATA.getIsbn()),
                        jsonPath("$.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$.publicationYear").value(BOOK_DATA.getPublicationYear()),
                        jsonPath("$.averageRating").value("0.0"),
                        jsonPath("$.authors[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.authors[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$.authors[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$.authors[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$.publisher.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.publisher.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$.publisher._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$.publisher._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._links.bookRatings.href").value(BOOK_DATA.getBookRatingHref())
        );
    }


    @Test
    void update_shouldReturnErrorMessage_whenDataIsInvalid() throws Exception {
        when(bookService.update(anyLong(), any(BookDto.class))).thenReturn(BOOK_DATA.getBook());
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/books/{id}", BOOK_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"id": "110464",
                             "isbn": "0736688390",
                             "title": " ",
                             "publicationYear": " ",
                             "authorsId": [],
                             "publisherId": "",
                             "image": {
                                 "imageUrlSmall": "",
                                 "imageUrlMedium": " ",
                                 "imageUrlLarge": "ftp://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"
                             }
                            }""")).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title").value("The title of the book is required and cannot be empty."),
                        jsonPath("$.publicationYear").value("The publication year is required and cannot be null."),
                        jsonPath("$.authorsId").value("At least one author is required for the book."),
                        jsonPath("$.publisherId").value("The publisher is required and cannot be null."),
                        jsonPath("$.['image.imageUrlSmall']").value("Image URL Small is required"),
                        jsonPath("$.['image.imageUrlMedium']").value("Image URL Medium is required"),
                        jsonPath("$.['image.imageUrlLarge']").value("Image URL Large must be a valid HTTP URL.")
                );
    }

    @Test
    void update_shouldReturnErrorMessage_whenIsbnAlreadyExist() throws Exception {
        when(bookService.update(anyLong(), any(BookDto.class))).thenReturn(BOOK_DATA.getBook());
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(BOOK_DATA.getBook()));

        mockMvc.perform(put("/api/v1/books/{id}", BOOK_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"id": "110",
                             "isbn": "0736688390",
                             "title": "Reversible Errors",
                             "publicationYear": "2003",
                             "authorsId": [4],
                             "publisherId": 1577,
                             "image": {
                                 "imageUrlSmall": "http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
                                 "imageUrlMedium": "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
                                 "imageUrlLarge": "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"
                             }
                            }""")).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.isbn").value("The provided ISBN already exists for another book.")
                );
    }

    @Test
    void delete_() throws Exception {
        mockMvc.perform(delete("/api/v1/books/{id}", BOOK_DATA.getId()))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void getFilteredBooks() throws Exception {
        when(bookService.getBooksByAuthorAndPublisher(anyList(), anyList(), anyInt(), anyString(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookRatingProjections());

        mockMvc.perform(get("/api/v1/books/filter-by").param("authorsId", "4").param("publishersId", "1577"))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value(BOOK_DATA.getPublicationYear()),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }
}