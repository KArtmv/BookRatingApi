package ua.foxminded.bookrating.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.bookrating.assembler.PublisherModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.dto.PublisherDto;
import ua.foxminded.bookrating.security.SecurityConfig;
import ua.foxminded.bookrating.service.PublisherService;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({PublisherModelAssembler.class, SimpleBookModelAssembler.class, SecurityConfig.class})
@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

    public static final PublisherData PUBLISHER_DATA = new PublisherData();
    public static final BookData BOOK_DATA = new BookData();
    public static final AuthorsData AUTHOR_DATA = new AuthorsData();

    @MockBean
    private PublisherService publisherService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll_shouldReturnPublishers_whenUseIsUnauthorized() throws Exception {
        when(publisherService.findAll(any(Pageable.class))).thenReturn(PUBLISHER_DATA.getPublishers());

        mockMvc.perform(get("/api/v1/publishers")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.publisherModelList[0].id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$._embedded.publisherModelList[0].name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.publisherModelList[0]._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._embedded.publisherModelList[0]._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$._embedded.publisherModelList[1].id").value(PUBLISHER_DATA.getId2()),
                        jsonPath("$._embedded.publisherModelList[1].name").value(PUBLISHER_DATA.getName2()),
                        jsonPath("$._embedded.publisherModelList[1]._links.self.href").value("http://localhost/api/v1/publishers/21"),
                        jsonPath("$._embedded.publisherModelList[1]._links.publisherBooks.href").value("http://localhost/api/v1/publishers/21/books"),
                        jsonPath("$.page.size").value("2"),
                        jsonPath("$.page.totalElements").value("100"),
                        jsonPath("$.page.totalPages").value("50"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getPublisherBooks_shouldReturnPublisherBooks__whenUseIsUnauthorized() throws Exception {
        when(publisherService.getAllBooksById(anyLong(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookPage());

        mockMvc.perform(get("/api/v1/publishers/{id}/books", PUBLISHER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHOR_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/publishers/1577/books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getPublisherContainName_shouldReturnPublisher_whenUseIsUnauthorized() throws Exception {
        when(publisherService.getByNameContaining(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(PUBLISHER_DATA.getPublisher()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/publishers/find-by-name").param("name", "book")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.publisherModelList[0].id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$._embedded.publisherModelList[0].name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.publisherModelList[0]._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._embedded.publisherModelList[0]._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_shouldReturnPublisher_whenUseIsUnauthorized() throws Exception {
        when(publisherService.findById(anyLong())).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(get("/api/v1/publishers/{id}", PUBLISHER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref())
                );
    }

    @Test
    void add_shouldReturnForbidden_whenUseIsUnauthorized() throws Exception {
        when(publisherService.create(any(PublisherDto.class))).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(post("/api/v1/publishers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Books on Tape\"}")).andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    void update_shouldReturnForbidden_whenUseIsUnauthorized() throws Exception {
        when(publisherService.update(anyLong(), any(PublisherDto.class))).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(put("/api/v1/publishers/{id}", PUBLISHER_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Books on Tape\"}")).andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    void delete_shouldReturnForbidden_whenUseIsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/publishers/{id}", PUBLISHER_DATA.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll_shouldReturn_shouldPerform_whenUserIsAuthorized() throws Exception {
        when(publisherService.findAll(any(Pageable.class))).thenReturn(PUBLISHER_DATA.getPublishers());

        mockMvc.perform(get("/api/v1/publishers").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.publisherModelList[0].id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$._embedded.publisherModelList[0].name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.publisherModelList[0]._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._embedded.publisherModelList[0]._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$._embedded.publisherModelList[1].id").value(PUBLISHER_DATA.getId2()),
                        jsonPath("$._embedded.publisherModelList[1].name").value(PUBLISHER_DATA.getName2()),
                        jsonPath("$._embedded.publisherModelList[1]._links.self.href").value("http://localhost/api/v1/publishers/21"),
                        jsonPath("$._embedded.publisherModelList[1]._links.publisherBooks.href").value("http://localhost/api/v1/publishers/21/books"),
                        jsonPath("$.page.size").value("2"),
                        jsonPath("$.page.totalElements").value("100"),
                        jsonPath("$.page.totalPages").value("50"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getPublisherBooks_shouldPerform_whenUserIsAuthorized() throws Exception {
        when(publisherService.getAllBooksById(anyLong(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookPage());

        mockMvc.perform(get("/api/v1/publishers/{id}/books", PUBLISHER_DATA.getId()).with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHOR_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/publishers/1577/books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorsContainName_shouldPerform_whenUserIsAuthorized() throws Exception {
        when(publisherService.getByNameContaining(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(PUBLISHER_DATA.getPublisher()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/publishers/find-by-name").param("name", "book").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.publisherModelList[0].id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$._embedded.publisherModelList[0].name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.publisherModelList[0]._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._embedded.publisherModelList[0]._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get__shouldPerform_whenUserIsAuthorized() throws Exception {
        when(publisherService.findById(anyLong())).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(get("/api/v1/publishers/{id}", PUBLISHER_DATA.getId()).with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref())
                );
    }

    @Test
    void add_shouldPerform_whenUserIsAuthorized() throws Exception {
        when(publisherService.create(any(PublisherDto.class))).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(post("/api/v1/publishers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Books on Tape\"}").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref())
                );
    }

    @Test
    void update_shouldPerform_whenUserIsAuthorized() throws Exception {
        when(publisherService.update(anyLong(), any(PublisherDto.class))).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(put("/api/v1/publishers/{id}", PUBLISHER_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Books on Tape\"}").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref())
                );
    }

    @Test
    void delete_shouldPerform_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/publishers/{id}", PUBLISHER_DATA.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void restore_shouldReturnUnauthorizedStatus_whenUserIsNotAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/publishers/{id}/restore", PUBLISHER_DATA.getId()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void restore_shouldDoNothing_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/publishers/{id}/restore", PUBLISHER_DATA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPublishersBooks_shouldReturnBadRequest_whenPublishersIsNotFounded() throws Exception {
        when(publisherService.getAllBooksById(anyLong(), any(Pageable.class)))
                .thenThrow(new EntityNotFoundException("Entity with id: " + PUBLISHER_DATA.getId() + " is not found"));

        mockMvc.perform(get("/api/v1/publishers/{id}/books", PUBLISHER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.type").value("about:blank"),
                        jsonPath("$.title").value("Not Found"),
                        jsonPath("$.status").value("404"),
                        jsonPath("$.detail").value("Entity with id: " + PUBLISHER_DATA.getId() + " is not found")
                );
    }

    @Test
    void get_shouldReturnPublishers_whenPublishersIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/publishers/{id}", "abc")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_shouldReturnPublishers_whenPublishersIdIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/publishers/{id}", "")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPublishersBooks_shouldReturnBadRequest_whenPublishersIdIsNotValid() throws Exception {
        mockMvc.perform(get("/api/v1/publishers/{id}/books", "abc")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPublishersBooks_shouldReturnBadRequest_whenPublishersIdIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/publishers/{id}/books", "")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void getPublishersContainName_shouldReturnBadRequest_whenNameIsEmptyOrBlank(String name) throws Exception {
        mockMvc.perform(get("/api/v1/publishers/find-by-name").param("name", name)).andDo(print())
                .andExpectAll(status().isBadRequest(),
                        content().string(containsString("name")),
                        content().string(containsString("Name cannot be blank or empty"))
                );
    }

    @Test
    void getDeletedPublisher_shouldReturnSoftDeletedAuthor_whenIsFounded() throws Exception {
        when(publisherService.getDeletedByName(PUBLISHER_DATA.getName())).thenReturn(PUBLISHER_DATA.getPublisher());

        mockMvc.perform(get("/api/v1/publishers/deleted")
                        .param("name", PUBLISHER_DATA.getName())
                        .with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PUBLISHER_DATA.getId()),
                        jsonPath("$.name").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._links.self.href").value(PUBLISHER_DATA.getSelfHref()),
                        jsonPath("$._links.publisherBooks.href").value(PUBLISHER_DATA.getPublisherBooksHref()),
                        jsonPath("$._links.restore.href").value(PUBLISHER_DATA.getSelfHref() + "/restore")
                );
    }

    @Test
    void getDeletedPublisher_shouldReturnSoftDeletedAuthor_whenIsNotFounded() throws Exception {
        when(publisherService.getDeletedByName(PUBLISHER_DATA.getName()))
                .thenThrow(new EntityNotFoundException("Deleted publisher not found with name: " + PUBLISHER_DATA.getName()));

        mockMvc.perform(get("/api/v1/publishers/deleted")
                        .param("name", PUBLISHER_DATA.getName())
                        .with(jwt())).andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.type").value("about:blank"),
                        jsonPath("$.title").value("Not Found"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.detail").value("Deleted publisher not found with name: " + PUBLISHER_DATA.getName()),
                        jsonPath("$.instance").value("/api/v1/publishers/deleted")
                );
    }

    @Test
    void getDeletedPublisher_shouldReturnForbidden_whenUserIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/publishers/deleted")
                        .param("name", PUBLISHER_DATA.getName())).andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void getDeletedPublisher_shouldBadRequest_whenNameIsBlank(String name) throws Exception {
        mockMvc.perform(get("/api/v1/publishers/deleted")
                        .param("name", name)
                        .with(jwt())).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.name").value("Name cannot be blank or empty")
                );
    }

    @Test
    void getDeletedPublisher_shouldBadRequest_whenNameIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/publishers/deleted")
                        .with(jwt())).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.name").value("Required parameter is missing")
                );
    }
}