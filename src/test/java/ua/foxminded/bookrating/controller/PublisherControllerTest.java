package ua.foxminded.bookrating.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.assembler.PublisherModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.service.PublisherService;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({PublisherModelAssembler.class, BookModelAssembler.class, SimpleBookModelAssembler.class})
@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

    public static final PublisherData PUBLISHER_DATA = new PublisherData();
    public static final BookData BOOK_DATA = new BookData();

    @MockBean
    private PublisherService publisherService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll_shouldReturn() throws Exception {
        when(publisherService.findAllPaginated(any(Pageable.class))).thenReturn(PUBLISHER_DATA.getPublishers());

        mockMvc.perform(get("/api/v1/publishers")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.publisherModelList[0].id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$._embedded.publisherModelList[0].name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.publisherModelList[0]._links.self.href").value("http://localhost/api/v1/publishers/1577"),
                        jsonPath("$._embedded.publisherModelList[0]._links.publisherBooks.href").value("http://localhost/api/v1/publishers/1577/books?desiredAverageRating=0"),
                        jsonPath("$._embedded.publisherModelList[1].id").value(PUBLISHER_DATA.getId2()),
                        jsonPath("$._embedded.publisherModelList[1].name").value(PUBLISHER_DATA.getName2()),
                        jsonPath("$._embedded.publisherModelList[1]._links.self.href").value("http://localhost/api/v1/publishers/21"),
                        jsonPath("$._embedded.publisherModelList[1]._links.publisherBooks.href").value("http://localhost/api/v1/publishers/21/books?desiredAverageRating=0"),
                        jsonPath("$.page.size").value("2"),
                        jsonPath("$.page.totalElements").value("100"),
                        jsonPath("$.page.totalPages").value("50"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getPublisherBooks() throws Exception {
        when(publisherService.getAllBooksById(anyLong(), anyInt(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookRatingProjections());

        mockMvc.perform(get("/api/v1/publishers/{id}/books", PUBLISHER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(110464),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value("Reversible Errors"),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value("Scott Turow"),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value("Books on Tape"),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value("2003"),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value("http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value("http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value("http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value("http://localhost/api/v1/books/110464"),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/publishers/1577/books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorsContainName() throws Exception {
        when(publisherService.getByNameContaining(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(PUBLISHER_DATA.getPublisher()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/publishers/find-by-name").param("name", "book")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.publisherModelList[0].id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$._embedded.publisherModelList[0].name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.publisherModelList[0]._links.self.href").value("http://localhost/api/v1/publishers/1577"),
                        jsonPath("$._embedded.publisherModelList[0]._links.publisherBooks.href").value("http://localhost/api/v1/publishers/1577/books?desiredAverageRating=0"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_() throws Exception {
        when(publisherService.findById(anyLong())).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(get("/api/v1/publishers/{id}", PUBLISHER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/publishers/1577"),
                        jsonPath("$._links.publisherBooks.href").value("http://localhost/api/v1/publishers/1577/books?desiredAverageRating=0")
                );
    }

    @Test
    void add() throws Exception {
        when(publisherService.save(any(Publisher.class))).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(post("/api/v1/publishers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Books on Tape\"}")).andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/publishers/1577"),
                        jsonPath("$._links.publisherBooks.href").value("http://localhost/api/v1/publishers/1577/books?desiredAverageRating=0")
                );
    }

    @Test
    void update() throws Exception {
        when(publisherService.update(anyLong(), any(Publisher.class))).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(put("/api/v1/publishers/{id}", PUBLISHER_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Books on Tape\"}")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/publishers/1577"),
                        jsonPath("$._links.publisherBooks.href").value("http://localhost/api/v1/publishers/1577/books?desiredAverageRating=0")
                );
    }

    @Test
    void delete_() throws Exception {
        mockMvc.perform(delete("/api/v1/publishers/{id}", PUBLISHER_DATA.getId()))
                .andExpect(status().isNoContent());
    }
}