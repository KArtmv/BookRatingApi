package ua.foxminded.bookrating.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.bookrating.assembler.FullRatingModelAssembler;
import ua.foxminded.bookrating.assembler.RatingModelAssembler;
import ua.foxminded.bookrating.assembler.SimpleBookModelAssembler;
import ua.foxminded.bookrating.assembler.UserModelAssembler;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.security.SecurityConfig;
import ua.foxminded.bookrating.service.RatingService;
import ua.foxminded.bookrating.util.book.BookData;
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

@Import({SimpleBookModelAssembler.class, UserModelAssembler.class, RatingModelAssembler.class, FullRatingModelAssembler.class, SecurityConfig.class})
@WebMvcTest(RatingController.class)
class RatingControllerTest {

    private static final RatingData RATING_DATA = new RatingData();
    private static final BookData BOOK_DATA = new BookData();
    private static final UserData USER_DATA = new UserData();

    @MockBean
    private RatingService ratingService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_shouldReturnRating_whenUserIsUnauthorized() throws Exception {
        when(ratingService.findById(anyLong())).thenReturn(RATING_DATA.getRating());

        mockMvc.perform(get("/api/v1/ratings/{id}", RATING_DATA.getId())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(RATING_DATA.getId()),
                        jsonPath("$.user.id").value(USER_DATA.getId()),
                        jsonPath("$.user.location").value(USER_DATA.getLocation()),
                        jsonPath("$.user.age").value(USER_DATA.getAge()),
                        jsonPath("$.user._links.ratedBooks.href").value(USER_DATA.getUserRatedBooksHref()),
                        jsonPath("$.user._links.self.href").value(USER_DATA.getSelfHref()),
                        jsonPath("$.book.id").value(BOOK_DATA.getId()),
                        jsonPath("$.book.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$.book.author").value(BOOK_DATA.getBook().getAuthors().stream().findFirst().get().getName()),
                        jsonPath("$.book.publisher").value(BOOK_DATA.getBook().getPublisher().getName()),
                        jsonPath("$.book.publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$.book.averageRating").value("0.0"),
                        jsonPath("$.book.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$.book.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$.book.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$.book._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$.rating").value(RATING_DATA.getUserRating()),
                        jsonPath("$._links.self.href").value(RATING_DATA.getSelfHref())
                );
    }

    @Test
    void add_shouldReturnUnauthorized_whenUserIsUnauthorized() throws Exception {
        when(ratingService.create(any(RatingDto.class))).thenReturn(RATING_DATA.getRating());

        mockMvc.perform(post("/api/v1/ratings").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                         "bookId": "110464",
                         "userId": "58792",
                         "bookRating": "7"
                        }
                        """)).andDo(print()).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void update_shouldReturnUnauthorized_whenUserIsUnauthorized() throws Exception {
        when(ratingService.update(anyLong(), any(RatingDto.class))).thenReturn(RATING_DATA.getUpdatedRating());

        mockMvc.perform(put("/api/v1/ratings/{id}", RATING_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "bookId": "",
                        "userId": "",
                        "bookRating": "5"
                                }""")).andDo(print()).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void delete_shouldReturnUnauthorized_whenUserIsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/ratings/{id}", RATING_DATA.getId())).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get_shouldReturnRating_whenUserIsAuthorized() throws Exception {
        when(ratingService.findById(anyLong())).thenReturn(RATING_DATA.getRating());

        mockMvc.perform(get("/api/v1/ratings/{id}", RATING_DATA.getId()).with(jwt())).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(RATING_DATA.getId()),
                        jsonPath("$.user.id").value(USER_DATA.getId()),
                        jsonPath("$.user.location").value(USER_DATA.getLocation()),
                        jsonPath("$.user.age").value(USER_DATA.getAge()),
                        jsonPath("$.user._links.ratedBooks.href").value(USER_DATA.getUserRatedBooksHref()),
                        jsonPath("$.user._links.self.href").value(USER_DATA.getSelfHref()),
                        jsonPath("$.book.id").value(BOOK_DATA.getId()),
                        jsonPath("$.book.title").value(BOOK_DATA.getTitle()),
                        jsonPath("$.book.author").value(BOOK_DATA.getBook().getAuthors().stream().findFirst().get().getName()),
                        jsonPath("$.book.publisher").value(BOOK_DATA.getBook().getPublisher().getName()),
                        jsonPath("$.book.publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                        jsonPath("$.book.averageRating").value("0.0"),
                        jsonPath("$.book.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                        jsonPath("$.book.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                        jsonPath("$.book.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                        jsonPath("$.book._links.self.href").value(BOOK_DATA.getSelfHref()),
                        jsonPath("$.rating").value(RATING_DATA.getUserRating()),
                        jsonPath("$._links.self.href").value(RATING_DATA.getSelfHref())
                );
    }

    @Test
    void add_shouldReturnRating_whenUserIsAuthorized() throws Exception {
        when(ratingService.create(any(RatingDto.class))).thenReturn(RATING_DATA.getRating());

        mockMvc.perform(post("/api/v1/ratings").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                         "bookId": "110464",
                         "userId": "58792",
                         "bookRating": "7"
                        }
                        """).with(jwt())).andDo(print()).andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(RATING_DATA.getId()),
                jsonPath("$.user.id").value(USER_DATA.getId()),
                jsonPath("$.user.location").value(USER_DATA.getLocation()),
                jsonPath("$.user.age").value(USER_DATA.getAge()),
                jsonPath("$.user._links.ratedBooks.href").value(USER_DATA.getUserRatedBooksHref()),
                jsonPath("$.user._links.self.href").value(USER_DATA.getSelfHref()),
                jsonPath("$.book.id").value(BOOK_DATA.getId()),
                jsonPath("$.book.title").value(BOOK_DATA.getTitle()),
                jsonPath("$.book.author").value(BOOK_DATA.getBook().getAuthors().stream().findFirst().get().getName()),
                jsonPath("$.book.publisher").value(BOOK_DATA.getBook().getPublisher().getName()),
                jsonPath("$.book.publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                jsonPath("$.book.averageRating").value("0.0"),
                jsonPath("$.book.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                jsonPath("$.book.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                jsonPath("$.book.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                jsonPath("$.book._links.self.href").value(BOOK_DATA.getSelfHref()),
                jsonPath("$.rating").value(RATING_DATA.getUserRating()),
                jsonPath("$._links.self.href").value(RATING_DATA.getSelfHref())
        );
    }

    @Test
    void update_shouldReturnRating_whenUserIsAuthorized() throws Exception {
        when(ratingService.update(anyLong(), any(RatingDto.class))).thenReturn(RATING_DATA.getUpdatedRating());

        mockMvc.perform(put("/api/v1/ratings/{id}", RATING_DATA.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "bookId": "",
                        "userId": "",
                        "bookRating": "5"
                                }""").with(jwt())).andDo(print()).andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(RATING_DATA.getId()),
                jsonPath("$.user.id").value(USER_DATA.getId()),
                jsonPath("$.user.location").value(USER_DATA.getLocation()),
                jsonPath("$.user.age").value(USER_DATA.getAge()),
                jsonPath("$.user._links.ratedBooks.href").value(USER_DATA.getUserRatedBooksHref()),
                jsonPath("$.user._links.self.href").value(USER_DATA.getSelfHref()),
                jsonPath("$.book.id").value(BOOK_DATA.getId()),
                jsonPath("$.book.title").value(BOOK_DATA.getTitle()),
                jsonPath("$.book.author").value(BOOK_DATA.getBook().getAuthors().stream().findFirst().get().getName()),
                jsonPath("$.book.publisher").value(BOOK_DATA.getBook().getPublisher().getName()),
                jsonPath("$.book.publicationYear").value(BOOK_DATA.getPublicationYear().toString()),
                jsonPath("$.book.averageRating").value("0.0"),
                jsonPath("$.book.image.imageUrlSmall").value(BOOK_DATA.getImage().getImageUrlSmall()),
                jsonPath("$.book.image.imageUrlMedium").value(BOOK_DATA.getImage().getImageUrlMedium()),
                jsonPath("$.book.image.imageUrlLarge").value(BOOK_DATA.getImage().getImageUrlLarge()),
                jsonPath("$.book._links.self.href").value(BOOK_DATA.getSelfHref()),
                jsonPath("$.rating").value(RATING_DATA.getUpdatedUserRating()),
                jsonPath("$._links.self.href").value(RATING_DATA.getSelfHref())
        );
    }

    @Test
    void delete_shouldDoNothing_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/ratings/{id}", RATING_DATA.getId()).with(jwt())).andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void get_shouldReturnBadRequest_whenRatingIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/ratings/abc")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_shouldReturnNotFound_whenRatingIdIsMissed() throws Exception {
        mockMvc.perform(get("/api/v1/ratings/")).andDo(print())
                .andExpect(status().isNotFound());
    }
}