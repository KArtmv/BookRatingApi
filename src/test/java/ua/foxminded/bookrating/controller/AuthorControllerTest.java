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
import ua.foxminded.bookrating.assembler.AuthorModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.dto.AuthorDto;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.security.SecurityConfig;
import ua.foxminded.bookrating.service.AuthorService;
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

@Import({AuthorModelAssembler.class, SimpleBookModelAssembler.class, SecurityConfig.class})
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final BookData BOOK_DATA = new BookData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    @MockBean
    private AuthorService authorService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll_shouldReturnAuthors_whenUserIsUnauthorized() throws Exception {
        when(authorService.findAll(any(Pageable.class))).thenReturn(AUTHORS_DATA.getAuthors());

        mockMvc.perform(get("/api/v1/authors")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.authorModelList[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$._embedded.authorModelList[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.authorModelList[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._embedded.authorModelList[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$._embedded.authorModelList[1].id").value(AUTHORS_DATA.getId2()),
                        jsonPath("$._embedded.authorModelList[1].name").value(AUTHORS_DATA.getName2()),
                        jsonPath("$._embedded.authorModelList[1]._links.self.href").value("http://localhost/api/v1/authors/2172"),
                        jsonPath("$._embedded.authorModelList[1]._links.authorBooks.href").value("http://localhost/api/v1/authors/2172/books"),
                        jsonPath("$.page.size").value("2"),
                        jsonPath("$.page.totalElements").value("100"),
                        jsonPath("$.page.totalPages").value("50"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorBooks_shouldReturnAuthorBooks_whenUserIsUnauthorized() throws Exception {
        when(authorService.getAllBooksById(anyLong(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookPage());

        mockMvc.perform(get("/api/v1/authors/{id}/books", AUTHORS_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/authors/4/books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorsContainName_shouldReturnAuthorByName_whenUserIsUnauthorized() throws Exception {
        when(authorService.getByNameContaining(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(AUTHORS_DATA.getAuthor()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/authors/find-by-name").param("name", "scott")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.authorModelList[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$._embedded.authorModelList[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.authorModelList[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._embedded.authorModelList[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_shouldReturnAuthor_whenUseIsUnauthorized() throws Exception {
        when(authorService.findById(anyLong())).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(get("/api/v1/authors/{id}", AUTHORS_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref())
                );
    }

    @Test
    void add_shouldReturnUnauthorized_whenUseIsUnauthorized() throws Exception {
        when(authorService.create(any(AuthorDto.class))).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(post("/api/v1/authors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Scott Turow\"}")).andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    void update_shouldReturnUnauthorized_whenUseIsUnauthorized() throws Exception {
        when(authorService.update(anyLong(), any(AuthorDto.class))).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(put("/api/v1/authors/{id}", AUTHORS_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Scott Turow\"}")).andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    void delete_shouldReturnUnauthorized_whenUseIsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/authors/{id}", AUTHORS_DATA.getId()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void getAll_shouldReturnAuthors_whenUserIsAuthorized() throws Exception {
        when(authorService.findAll(any(Pageable.class))).thenReturn(AUTHORS_DATA.getAuthors());

        mockMvc.perform(get("/api/v1/authors").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.authorModelList[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$._embedded.authorModelList[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.authorModelList[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._embedded.authorModelList[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$._embedded.authorModelList[1].id").value(AUTHORS_DATA.getId2()),
                        jsonPath("$._embedded.authorModelList[1].name").value(AUTHORS_DATA.getName2()),
                        jsonPath("$._embedded.authorModelList[1]._links.self.href").value("http://localhost/api/v1/authors/2172"),
                        jsonPath("$._embedded.authorModelList[1]._links.authorBooks.href").value("http://localhost/api/v1/authors/2172/books"),
                        jsonPath("$.page.size").value("2"),
                        jsonPath("$.page.totalElements").value("100"),
                        jsonPath("$.page.totalPages").value("50"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorBooks_shouldReturnAuthorsBooks_whenUserIsAuthorized() throws Exception {
        when(authorService.getAllBooksById(anyLong(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookPage());

        mockMvc.perform(get("/api/v1/authors/{id}/books", AUTHORS_DATA.getId()).with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/authors/4/books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorsContainName_shouldReturnAuthorByName_whenUserIsAuthorized() throws Exception {
        when(authorService.getByNameContaining(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(AUTHORS_DATA.getAuthor()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/authors/find-by-name").param("name", "scott").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.authorModelList[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$._embedded.authorModelList[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.authorModelList[0]._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._embedded.authorModelList[0]._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref()),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_shouldReturnAuthor_whenUserIsAuthorized() throws Exception {
        when(authorService.findById(anyLong())).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(get("/api/v1/authors/{id}", AUTHORS_DATA.getId()).with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref())
                );
    }

    @Test
    void add_shouldReturnAuthor_whenUserIsAuthorized() throws Exception {
        when(authorService.create(any(AuthorDto.class))).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(post("/api/v1/authors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Scott Turow\"}").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref())
                );
    }

    @Test
    void update_shouldReturnAuthor_whenUserIsAuthorized() throws Exception {
        when(authorService.update(anyLong(), any(AuthorDto.class))).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(put("/api/v1/authors/{id}", AUTHORS_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Scott Turow\"}").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._links.self.href").value(AUTHORS_DATA.getSelfHref()),
                        jsonPath("$._links.authorBooks.href").value(AUTHORS_DATA.getAuthorBooksHref())
                );
    }

    @Test
    void delete_shouldDoNothing_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/authors/{id}", AUTHORS_DATA.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void restore_shouldReturnUnauthorizedStatus_whenUserIsNotAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/authors/{id}/restore", AUTHORS_DATA.getId()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void restore_shouldDoNothing_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/authors/{id}/restore", AUTHORS_DATA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAuthorBooks_shouldReturnBadRequest_whenAuthorIsNotFounded() throws Exception {
        when(authorService.getAllBooksById(anyLong(), any(Pageable.class)))
                .thenThrow(new EntityNotFoundException("Entity with id: " + AUTHORS_DATA.getId() + " is not found"));

        mockMvc.perform(get("/api/v1/authors/{id}/books", AUTHORS_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.type").value("about:blank"),
                        jsonPath("$.title").value("Not Found"),
                        jsonPath("$.status").value("404"),
                        jsonPath("$.detail").value("Entity with id: " + AUTHORS_DATA.getId() + " is not found")
                );
    }

    @Test
    void get_shouldReturnAuthor_whenAuthorIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/authors/abc")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_shouldReturnAuthor_whenAuthorIdIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/authors/")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAuthorBooks_shouldReturnBadRequest_whenAuthorIdIsNotValid() throws Exception {
        mockMvc.perform(get("/api/v1/authors/abc/books")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAuthorBooks_shouldReturnBadRequest_whenAuthorIdIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/authors//books")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void getAuthorsContainName_shouldReturnException_whenNameIsEmptyOrBlank(String name) throws Exception {
        mockMvc.perform(get("/api/v1/authors/find-by-name").param("name", name)).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(containsString("name")),
                        content().string(containsString("Name cannot be blank or empty"))
                );
    }
}