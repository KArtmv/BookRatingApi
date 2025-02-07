package ua.foxminded.bookrating.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.bookrating.assembler.RatingOfBookModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.assembler.UserModelAssembler;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.security.SecurityConfig;
import ua.foxminded.bookrating.service.UserService;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;
import ua.foxminded.bookrating.util.rating.RatingData;
import ua.foxminded.bookrating.util.user.UserData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({UserModelAssembler.class, RatingOfBookModelAssembler.class, SimpleBookModelAssembler.class, SecurityConfig.class})
@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final RatingData RATING_DATA = new RatingData();
    private static final UserData USER_DATA = new UserData();
    private static final BookData BOOK_DATA = new BookData();
    public static final AuthorsData AUTHORS_DATA = new AuthorsData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRatedBooksByUser_shouldReturnRatedBooksByUser_whenUserIsUnauthorized() throws Exception {
        when(userService.findRatedBooksByUser(anyLong(), any(PageRequest.class))).thenReturn(RATING_DATA.getRatings());

        mockMvc.perform(get("/api/v1/users/{id}/rated-books", USER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.ratingModelList[0].book.id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.ratingModelList[0].book.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.ratingModelList[0].book.author").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.ratingModelList[0].book.publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.ratingModelList[0].book.publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$._embedded.ratingModelList[0].book.averageRating").value(0.0),
                        jsonPath("$._embedded.ratingModelList[0].book.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.ratingModelList[0].book.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.ratingModelList[0].book.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.ratingModelList[0].book._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$._embedded.ratingModelList[0].rating").value(RATING_DATA.getUserRating()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/users/58792/rated-books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_shouldReturnUser_whenUserIsUnauthorized() throws Exception {
        when(userService.findById(anyLong())).thenReturn(USER_DATA.getUser());

        mockMvc.perform(get("/api/v1/users/{id}", USER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(USER_DATA.getId()),
                        jsonPath("$.location").value(USER_DATA.getLocation()),
                        jsonPath("$.age").value(USER_DATA.getAge()),
                        jsonPath("$._links.ratedBooks.href").value(USER_DATA.getUserRatedBooksHref()),
                        jsonPath("$._links.self.href").value(USER_DATA.getSelfHref())
                );
    }

    @Test
    void add_shouldReturnUnauthorizedStatus_whenUserIsUnauthorized() throws Exception {
        when(userService.save(any(User.class))).thenReturn(USER_DATA.getUser());

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("""
                        {"age": "21",
                        "location": "Fenton, Michigan, Usa"
                        }""")).andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    void update_shouldReturnUnauthorizedStatus_whenUserIsUnauthorized() throws Exception {
        when(userService.update(anyLong(), any(User.class))).thenReturn(USER_DATA.getUser());

        mockMvc.perform(put("/api/v1/users/{id}", USER_DATA.getId()).contentType(MediaType.APPLICATION_JSON).content("""
                        {"age": "21",
                        "location": "Fenton, Michigan, Usa"
                        }""")).andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    void delete_shouldReturnUnauthorizedStatus_whenUserIsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", USER_DATA.getId())).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRatedBooksByUser_shouldReturnRatedBooksByUser_whenUserIsAuthorized() throws Exception {
        when(userService.findRatedBooksByUser(anyLong(), any(PageRequest.class))).thenReturn(RATING_DATA.getRatings());

        mockMvc.perform(get("/api/v1/users/{id}/rated-books", USER_DATA.getId()).with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.ratingModelList[0].book.id").value(BOOK_DATA.getId()),
                        jsonPath("$._embedded.ratingModelList[0].book.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$._embedded.ratingModelList[0].book.author").value(AUTHORS_DATA.getName()),
                        jsonPath("$._embedded.ratingModelList[0].book.publisher").value(PUBLISHER_DATA.getName()),
                        jsonPath("$._embedded.ratingModelList[0].book.publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$._embedded.ratingModelList[0].book.averageRating").value(0.0),
                        jsonPath("$._embedded.ratingModelList[0].book.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$._embedded.ratingModelList[0].book.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$._embedded.ratingModelList[0].book.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$._embedded.ratingModelList[0].book._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$._embedded.ratingModelList[0].rating").value(RATING_DATA.getUserRating()),
                        jsonPath("$._links.self.href").value("http://localhost/api/v1/users/58792/rated-books?page=0&size=10"),
                        jsonPath("$.page.size").value("10"),
                        jsonPath("$.page.totalElements").value("1"),
                        jsonPath("$.page.totalPages").value("1"),
                        jsonPath("$.page.number").value("0")
                );
    }

    @Test
    void get_shouldReturnUser_whenUserIsAuthorized() throws Exception {
        when(userService.findById(anyLong())).thenReturn(USER_DATA.getUser());

        mockMvc.perform(get("/api/v1/users/{id}", USER_DATA.getId()).with(jwt())).andDo(print())
                .andExpectAll(
                        jsonPath("$.id").value(USER_DATA.getId()),
                        jsonPath("$.location").value(USER_DATA.getLocation()),
                        jsonPath("$.age").value(USER_DATA.getAge()),
                        jsonPath("$._links.ratedBooks.href").value(USER_DATA.getUserRatedBooksHref()),
                        jsonPath("$._links.self.href").value(USER_DATA.getSelfHref())
                );
    }

    @Test
    void add_shouldReturnUser_whenUserIsAuthorized() throws Exception {
        when(userService.save(any(User.class))).thenReturn(USER_DATA.getUser());

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("""
                        {"age": "21",
                        "location": "Fenton, Michigan, Usa"
                        }""").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.age").value(USER_DATA.getAge()),
                        jsonPath("$.location").value(USER_DATA.getLocation())
                );
    }

    @Test
    void add_shouldBedRequest_whenUserDataIsMissed() throws Exception {
        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("""
                        {"age": "",
                        "location": ""
                        }""").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.location").value("Location can't be blank")
                );
    }

    @Test
    void update_shouldReturnUser_whenUserIsAuthorized() throws Exception {
        when(userService.update(anyLong(), any(User.class))).thenReturn(USER_DATA.getUser());

        mockMvc.perform(put("/api/v1/users/{id}", USER_DATA.getId()).contentType(MediaType.APPLICATION_JSON).content("""
                        {"age": "21",
                        "location": "Fenton, Michigan, Usa"
                        }""").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.age").value(USER_DATA.getAge()),
                        jsonPath("$.location").value(USER_DATA.getLocation())
                );
    }

    @Test
    void update_shouldBedRequest_whenUserDataIsMissed() throws Exception {
        when(userService.update(anyLong(), any(User.class))).thenReturn(USER_DATA.getUser());

        mockMvc.perform(put("/api/v1/users/{id}", USER_DATA.getId()).contentType(MediaType.APPLICATION_JSON).content("""
                        {"age": "",
                        "location": ""
                        }""").with(jwt())).andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.location").value("Location can't be blank")
                );
    }

    @Test
    void delete_shouldDoNoting_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", USER_DATA.getId()).with(jwt())).andDo(print())
                .andExpect(status().isNoContent());
    }


    @Test
    void restore_shouldReturnUnauthorizedStatus_whenUserIsNotAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/users/{id}/restore", USER_DATA.getId()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void restore_shouldDoNothing_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/users/{id}/restore", USER_DATA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRatedBooksByUser_shouldReturnBadRequest_whenUserIsNotFounded() throws Exception {
        when(userService.findRatedBooksByUser(anyLong(), any(Pageable.class)))
                .thenThrow(new EntityNotFoundException("Entity with id: " + USER_DATA.getId() + " is not found"));

        mockMvc.perform(get("/api/v1/users/{id}/rated-books", USER_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.type").value("about:blank"),
                        jsonPath("$.title").value("Not Found"),
                        jsonPath("$.status").value("404"),
                        jsonPath("$.detail").value("Entity with id: " + USER_DATA.getId() + " is not found")
                );
    }

    @Test
    void get_shouldReturnBadRequest_whenUserIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/users/abc")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_shouldReturnNotFound_whenUserIdIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/users/")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getRatedBooksByUser_shouldReturnBadRequest_whenUserIdIsNotValid() throws Exception {
        mockMvc.perform(get("/api/v1/users/abc/rated-books")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRatedBooksByUser_shouldReturnBadRequest_whenUserIdIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/users//rated-books")).andDo(print())
                .andExpect(status().isBadRequest());
    }
}