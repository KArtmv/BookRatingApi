package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.repo.BookRepository;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.PublisherService;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {

    public static final BookData BOOK_DATA = new BookData();
    public static final PublisherData PUBLISHER_DATA = new PublisherData();
    public static final AuthorsData AUTHOR_DATA = new AuthorsData();

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private PublisherService publisherService;
    @MockBean
    private AuthorService authorService;
    @Autowired
    private BookService bookService;

    @Test
    void findAllPaginated_shouldReturnPaginatedBooks_whenInvoked() {
        when(bookRepository.findAllPaginated(anyInt(), any(Pageable.class))).thenReturn(mock(Page.class));

        bookService.findAllPaginated(0, Pageable.unpaged());

        verify(bookRepository).findAllPaginated(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void save_shouldThrowsException_whenBookIsbnIsExist() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(BOOK_DATA.getBook()));

        assertThrows(EntityExistsException.class, () -> bookService.save(BOOK_DATA.bookDto()));

        verify(bookRepository).findByIsbn(anyString());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void save_shouldReturnSavedBook_whenBookIsbnIsNotExist() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());
        when(publisherService.findById(anyLong())).thenReturn(PUBLISHER_DATA.getPublisher());
        when(authorService.findById(anyLong())).thenReturn(AUTHOR_DATA.getAuthor());

        bookService.save(BOOK_DATA.bookDto());

        verify(bookRepository).findByIsbn(anyString());
        verify(publisherService).findById(anyLong());
        verify(authorService).findById(anyLong());
        var argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(argumentCaptor.capture());
        Book value = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(value.getIsbn()).isEqualTo(BOOK_DATA.getIsbn());
            assertThat(value.getTitle()).isEqualTo(BOOK_DATA.getTitle());
            assertThat(value.getPublicationYear()).isEqualTo(BOOK_DATA.getPublicationYear());
            assertThat(value.getPublisher()).isEqualTo(PUBLISHER_DATA.getPublisher());
            assertThat(value.getAuthors()).contains(AUTHOR_DATA.getAuthor());
            assertThat(value.getImage()).isEqualTo(BOOK_DATA.getImage());
        });
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void update_shouldThrowsException_whenBookIsNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.update(BOOK_DATA.getId(), BOOK_DATA.bookDtoUpdate()));

        verify(bookRepository).findById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void update_shouldReturnUpdatedBook_whenBookIsFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(BOOK_DATA.getBook()));
        when(publisherService.findById(anyLong())).thenReturn(PUBLISHER_DATA.getPublisher());
        when(authorService.findById(anyLong())).thenReturn(AUTHOR_DATA.getAuthor());

        bookService.update(BOOK_DATA.getId(), BOOK_DATA.bookDtoUpdate());

        verify(bookRepository).findById(anyLong());
        verify(publisherService).findById(anyLong());
        verify(authorService).findById(anyLong());
        var argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(argumentCaptor.capture());
        Book value = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(value.getIsbn()).isEqualTo(BOOK_DATA.getUpdatedIsbn());
            assertThat(value.getTitle()).isEqualTo(BOOK_DATA.getUpdatedTitle());
            assertThat(value.getPublicationYear()).isEqualTo(BOOK_DATA.getUpdatedPublicationYear());
            assertThat(value.getPublisher()).isEqualTo(PUBLISHER_DATA.getPublisher());
            assertThat(value.getAuthors()).contains(AUTHOR_DATA.getAuthor());
            assertThat(value.getImage()).isEqualTo(BOOK_DATA.getImage());
        });
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getByIsbn_shouldReturnBook_whenBookIsFound() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(BOOK_DATA.getBook()));

        bookService.getByIsbn(BOOK_DATA.getIsbn());

        verify(bookRepository).findByIsbn(anyString());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getByIsbn_shouldThrowsException_whenBookIsNotFound() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getByIsbn(BOOK_DATA.getIsbn()));

        verify(bookRepository).findByIsbn(anyString());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getByTitleContaining_shouldReturnPagedResult_whenInvoked() {
        when(bookRepository.findByTitleContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(mock(Page.class));

        bookService.getByTitleContaining(BOOK_DATA.getTitle(), Pageable.unpaged());

        verify(bookRepository).findByTitleContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void getBooksByAuthorAndPublisher_shouldReturnPagedResult_whenInvoked() {
        when(bookRepository.findByAuthorsOrPublisherIn(anyList(), anyList(), anyInt(), anyString(), any(Pageable.class))).thenReturn(mock(Page.class));
        when(authorService.findById(anyLong())).thenReturn(AUTHOR_DATA.getAuthor());
        when(publisherService.findById(anyLong())).thenReturn(PUBLISHER_DATA.getPublisher());

        bookService.getBooksByAuthorAndPublisher(Collections.singletonList(AUTHOR_DATA.getId()),
                Collections.singletonList(PUBLISHER_DATA.getId()),
                0, BOOK_DATA.getTitle(), Pageable.unpaged());

        verify(bookRepository).findByAuthorsOrPublisherIn(anyList(), anyList(), anyInt(), anyString(), any(Pageable.class));
        verify(authorService).findById(anyLong());
        verify(publisherService).findById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getBooksByAuthorAndPublisher_shouldThrowsException_whenAuthorIsNotFound() {
        when(authorService.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookService.getBooksByAuthorAndPublisher(Collections.singletonList(AUTHOR_DATA.getId()),
                Collections.singletonList(PUBLISHER_DATA.getId()),
                0, BOOK_DATA.getTitle(), Pageable.unpaged()));

        verify(authorService).findById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getBooksByAuthorAndPublisher_shouldThrowsException_whenPublisherIsNotFound() {
        when(publisherService.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookService.getBooksByAuthorAndPublisher(Collections.singletonList(AUTHOR_DATA.getId()),
                Collections.singletonList(PUBLISHER_DATA.getId()),
                0, BOOK_DATA.getTitle(), Pageable.unpaged()));

        verify(publisherService).findById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getBooksByAuthorAndPublisher_shouldReturnPagedResult_whenAuthorAndPublisherAreEmpty() {
        when(bookRepository.findByAuthorsOrPublisherIn(anyList(), anyList(), anyInt(), anyString(), any(Pageable.class))).thenReturn(mock(Page.class));

        bookService.getBooksByAuthorAndPublisher(Collections.emptyList(), Collections.emptyList(),
                0, BOOK_DATA.getTitle(), Pageable.unpaged());

        verify(bookRepository).findByAuthorsOrPublisherIn(anyList(), anyList(), anyInt(), anyString(), any(Pageable.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void findAll_shouldListOfBooks_whenInvoked() {
        when(bookRepository.findAll()).thenReturn(mock(List.class));

        bookService.findAll();

        verify(bookRepository).findAll();
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void delete_shouldDeleteBook_whenInvoked() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(BOOK_DATA.getBook()));

        bookService.delete(BOOK_DATA.getId());

        verify(bookRepository).findById(anyLong());
        verify(bookRepository).delete(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getRatingsByBookId_shouldReturnPagedResult_whenInvoked() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(BOOK_DATA.getBook()));
        when(bookRepository.findBookRatings(any(Book.class), any(Pageable.class))).thenReturn(mock(Page.class));

        bookService.getRatingsByBookId(BOOK_DATA.getId(), Pageable.unpaged());

        verify(bookRepository).findById(anyLong());
        verify(bookRepository).findBookRatings(any(Book.class), any(Pageable.class));
        verifyNoMoreInteractions(bookRepository);
    }
}