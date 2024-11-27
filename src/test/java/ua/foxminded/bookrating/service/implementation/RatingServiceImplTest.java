package ua.foxminded.bookrating.service.implementation;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.repo.RatingRepository;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.RatingService;
import ua.foxminded.bookrating.service.UserService;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.rating.RatingData;
import ua.foxminded.bookrating.util.user.UserData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = RatingServiceImpl.class)
class RatingServiceImplTest {

    public static final RatingData RATING_DATA = new RatingData();
    public static final UserData USER_DATA = new UserData();
    public static final BookData BOOK_DATA = new BookData();

    @MockBean
    private BookService bookService;
    @MockBean
    private UserService userService;
    @MockBean
    private RatingRepository ratingRepository;
    @Autowired
    private RatingService ratingService;

    @Test
    void findById_shouldReturnRating_whenIsFound() {
        when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(RATING_DATA.getRating()));

        ratingService.findById(RATING_DATA.getId());

        verify(ratingRepository).findById(anyLong());
        verifyNoMoreInteractions(ratingRepository);
    }

    @Test
    void save_shouldSaveRating_whenIsInvoke() {
        when(userService.findById(anyLong())).thenReturn(USER_DATA.getUser());
        when(bookService.findById(anyLong())).thenReturn(BOOK_DATA.getBook());

        ratingService.save(RATING_DATA.getRatingDto());

        verify(userService).findById(anyLong());
        verify(bookService).findById(anyLong());
        verify(ratingRepository).save(any(Rating.class));
        verifyNoMoreInteractions(ratingRepository);
    }

    @Test
    void update_shouldUpdateRating_whenIsInvoke() {
        when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(RATING_DATA.getRating()));
        when(userService.findById(anyLong())).thenReturn(USER_DATA.getUser());
        when(bookService.findById(anyLong())).thenReturn(BOOK_DATA.getBook());

        ratingService.update(RATING_DATA.getId(), RATING_DATA.getUpdatedRating());

        verify(ratingRepository).findById(anyLong());
        var argumentCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(argumentCaptor.capture());
        var result = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(result.getId()).isEqualTo(RATING_DATA.getId());
            assertThat(result.getUser()).isEqualTo(USER_DATA.getUser());
            assertThat(result.getBook()).isEqualTo(BOOK_DATA.getBook());
            assertThat(result.getBookRating()).isEqualTo(RATING_DATA.getUpdatedRating());
        });
        verifyNoMoreInteractions(ratingRepository);
    }

    @Test
    void delete_shouldDeleteRating_whenIsInvoke() {
        when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(RATING_DATA.getRating()));

        ratingService.delete(RATING_DATA.getId());

        verify(ratingRepository).findById(anyLong());
        verify(ratingRepository).delete(any(Rating.class));
        verifyNoMoreInteractions(ratingRepository);
    }
}