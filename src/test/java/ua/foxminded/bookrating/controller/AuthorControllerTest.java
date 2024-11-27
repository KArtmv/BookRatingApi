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
import ua.foxminded.bookrating.assembler.AuthorModelAssembler;
import ua.foxminded.bookrating.assembler.BookModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({AuthorModelAssembler.class, BookModelAssembler.class, SimpleBookModelAssembler.class})
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final BookData BOOK_DATA = new BookData();

    @MockBean
    private AuthorService authorService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll_shouldReturn() throws Exception {
        when(authorService.findAllPaginated(any(Pageable.class))).thenReturn(AUTHORS_DATA.getAuthors());

        mockMvc.perform(get("/api/v1/authors")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.authorModelList[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$._embedded.authorModelList[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.authorModelList[0]._links.self.href").value("http://localhost/api/v1/authors/4"),
                        jsonPath("$._embedded.authorModelList[0]._links.authorBooks.href").value("http://localhost/api/v1/authors/4/books?desiredAverageRating=0"),
                        jsonPath("$._embedded.authorModelList[1].id").value(AUTHORS_DATA.getId2()),
                        jsonPath("$._embedded.authorModelList[1].name").value(AUTHORS_DATA.getName2()),
                        jsonPath("$._embedded.authorModelList[1]._links.self.href").value("http://localhost/api/v1/authors/2172"),
                        jsonPath("$._embedded.authorModelList[1]._links.authorBooks.href").value("http://localhost/api/v1/authors/2172/books?desiredAverageRating=0"),
                        jsonPath("$.page.size").value("2"),
                        jsonPath("$.page.totalElements").value("100"),
                        jsonPath("$.page.totalPages").value("50"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorBooks() throws Exception {
        when(authorService.getAllBooksById(anyLong(), anyInt(), any(Pageable.class))).thenReturn(BOOK_DATA.getBookRatingProjections());

        mockMvc.perform(get("/api/v1/authors/{id}/books", AUTHORS_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.simpleBookModelList[0].id").value(110464),
                        jsonPath("$._embedded.simpleBookModelList[0].title").value("Reversible Errors"),
                        jsonPath("$._embedded.simpleBookModelList[0].author[0]").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.simpleBookModelList[0].publisher").value("Books on Tape"),
                        jsonPath("$._embedded.simpleBookModelList[0].publicationYear").value("2003"),
                        jsonPath("$._embedded.simpleBookModelList[0].averageRating").value("0.0"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlSmall").value("http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlMedium").value("http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg"),
                        jsonPath("$._embedded.simpleBookModelList[0].image.imageUrlLarge").value("http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg"),
                        jsonPath("$._embedded.simpleBookModelList[0]._links.self.href").value("http://localhost/api/v1/books/110464"),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/authors/4/books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void getAuthorsContainName() throws Exception {
        when(authorService.getByNameContaining(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(AUTHORS_DATA.getAuthor()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/authors/find-by-name").param("name", "scott")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.authorModelList[0].id").value(AUTHORS_DATA.getId()),
                        jsonPath("$._embedded.authorModelList[0].name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.authorModelList[0]._links.self.href").value("http://localhost/api/v1/authors/4"),
                        jsonPath("$._embedded.authorModelList[0]._links.authorBooks.href").value("http://localhost/api/v1/authors/4/books?desiredAverageRating=0"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_() throws Exception {
        when(authorService.findById(anyLong())).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(get("/api/v1/authors/{id}", AUTHORS_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/authors/4"),
                        jsonPath("$._links.authorBooks.href").value("http://localhost/api/v1/authors/4/books?desiredAverageRating=0")
                );
    }

    @Test
    void add() throws Exception {
        when(authorService.save(any(Author.class))).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(post("/api/v1/authors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Scott Turow\"}")).andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/authors/4"),
                        jsonPath("$._links.authorBooks.href").value("http://localhost/api/v1/authors/4/books?desiredAverageRating=0")
                );
    }

    @Test
    void update() throws Exception {
        when(authorService.update(anyLong(), any(Author.class))).thenReturn(AUTHORS_DATA.getAuthor());

        mockMvc.perform(put("/api/v1/authors/{id}", AUTHORS_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\" : \"Scott Turow\"}")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(AUTHORS_DATA.getId()),
                        jsonPath("$.name").value(AUTHORS_DATA.getName()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/authors/4"),
                        jsonPath("$._links.authorBooks.href").value("http://localhost/api/v1/authors/4/books?desiredAverageRating=0")
                );
    }

    @Test
    void delete_() throws Exception {
        mockMvc.perform(delete("/api/v1/authors/{id}", AUTHORS_DATA.getId()))
                .andExpect(status().isNoContent());
    }
}