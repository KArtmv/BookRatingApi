package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.repo.BookRepository;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.PublisherService;
import ua.foxminded.bookrating.util.author.AuthorsData;
import ua.foxminded.bookrating.util.book.BookData;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
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
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(mock(Page.class));

        bookService.findAll(Pageable.unpaged());

        verify(bookRepository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void crate_shouldReturnSavedBook_whenBookIsbnIsNotExist() {
        when(publisherService.findByNameOrSave(anyString())).thenReturn(PUBLISHER_DATA.getPublisher());
        when(authorService.findByNameOrSave(anyString())).thenReturn(AUTHOR_DATA.getAuthor());

        bookService.create(BOOK_DATA.bookDto());

        verify(publisherService).findByNameOrSave(PUBLISHER_DATA.getName());
        verify(authorService).findByNameOrSave(AUTHOR_DATA.getName());
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

        try {
            bookService.update(BOOK_DATA.getId(), BOOK_DATA.bookDtoUpdate());
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Entity with id: " + BOOK_DATA.getId() + " is not found");
        }

        verify(bookRepository).findById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void update_shouldReturnUpdatedBook_whenBookIsFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(BOOK_DATA.getBook()));
        when(publisherService.findByNameOrSave(anyString())).thenReturn(PUBLISHER_DATA.getPublisher());
        when(authorService.findByNameOrSave(anyString())).thenReturn(AUTHOR_DATA.getAuthor());

        bookService.update(BOOK_DATA.getId(), BOOK_DATA.bookDtoUpdate());

        verify(bookRepository).findById(BOOK_DATA.getId());
        verify(publisherService).findByNameOrSave(PUBLISHER_DATA.getName());
        verify(authorService).findByNameOrSave(AUTHOR_DATA.getName());
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

        try {
            bookService.getByIsbn(BOOK_DATA.getIsbn());
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("A book with the given ISBN: " + BOOK_DATA.getIsbn() + " is not found");
        }

        verify(bookRepository).findByIsbn(anyString());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getBooksWithFilters_shouldFilterBooksBasedOnGivenCriteria_whenInvoked() {
        when(authorService.findById(anyLong())).thenReturn(AUTHOR_DATA.getAuthor());
        when(publisherService.findById(anyLong())).thenReturn(PUBLISHER_DATA.getPublisher());
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mock(Page.class));

        bookService.getBooksWithFilters(BOOK_DATA.getTitle(), List.of(AUTHOR_DATA.getId()), List.of(PUBLISHER_DATA.getId()),
                BOOK_DATA.getPublicationYear().getValue(), 0, Pageable.unpaged());

        verify(authorService).findById(anyLong());
        verify(publisherService).findById(anyLong());
        verify(bookRepository).findAll(any(Specification.class), any(Pageable.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getBooksWithFilters_shouldFilterBooksBasedOnGivenCriteria_whenAuthorAndPublisherIdsAreEmpty() {
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mock(Page.class));

        bookService.getBooksWithFilters(BOOK_DATA.getTitle(), null, null,
                BOOK_DATA.getPublicationYear().getValue(), 0, Pageable.unpaged());

        verify(authorService, never()).findById(anyLong());
        verify(publisherService, never()).findById(anyLong());
        verify(bookRepository).findAll(any(Specification.class), any(Pageable.class));
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

    @Test
    void save_shouldReturnSavedBook_whenInvoke() {
        when(bookRepository.save(any(Book.class))).thenReturn(BOOK_DATA.getBook());

        bookService.save(BOOK_DATA.getNewBook());

        verify(bookRepository).save(BOOK_DATA.getNewBook());
    }

    @Test
    void getDeletedByBookByIsbn_shouldReturnDeletedAuthor_whenIsFound() {
        when(bookRepository.findDeletedBookByIsbn(anyString())).thenReturn(Optional.of(BOOK_DATA.getBook()));

        bookService.getDeletedBooksByIsbn(BOOK_DATA.getIsbn());

        verify(bookRepository).findDeletedBookByIsbn(BOOK_DATA.getIsbn());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getDeletedByBookByIsbn_shouldThrowException_whenIsNotFound() {
        when(bookRepository.findDeletedBookByIsbn(anyString())).thenReturn(Optional.empty());

        try {
            bookService.getDeletedBooksByIsbn(BOOK_DATA.getIsbn());
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Deleted book with ISBN '" + BOOK_DATA.getIsbn() + "' was not found");
        }
    }
}