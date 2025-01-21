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
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.repo.AuthorRepository;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.util.author.AuthorsData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AuthorServiceImpl.class)
class AuthorServiceImplTest {

    public static final AuthorsData AUTHORS_DATA = new AuthorsData();

    @MockBean
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;

    @Test
    void findAllPaginated_shouldReturnAllAuthors() {
        when(authorRepository.findAllPaginated(any(Pageable.class))).thenReturn(mock(Page.class));

        authorService.findAllPaginated(Pageable.unpaged());

        verify(authorRepository).findAllPaginated(any(Pageable.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void save_shouldThrowsException_whenAuthorIsExist() {
        when(authorRepository.findByName(anyString())).thenReturn(Optional.of(AUTHORS_DATA.getAuthor()));

        try {
            authorService.save(AUTHORS_DATA.getAuthor());
        } catch (EntityExistsException e) {
            assertThat(e.getMessage()).isEqualTo(AUTHORS_DATA.getAuthor().getName() + " already exists");
        }

        verify(authorRepository).findByName(anyString());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void save_shouldSavedAuthor_whenAuthorIsNotExist() {
        when(authorRepository.findByName(anyString())).thenReturn(Optional.empty());

        authorService.save(AUTHORS_DATA.getAuthor());

        verify(authorRepository).findByName(anyString());
        verify(authorRepository).save(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void update_shouldThrowsException_whenAuthorIsNotExist() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            authorService.update(AUTHORS_DATA.getId(), AUTHORS_DATA.getNewAuthor());
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Entity with id: " + AUTHORS_DATA.getId() + " is not found");
        }

        verify(authorRepository).findById(anyLong());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void update_shouldReturnUpdatedAuthor_whenAuthorIsExist() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(AUTHORS_DATA.getAuthor()));

        authorService.update(AUTHORS_DATA.getId(), AUTHORS_DATA.getUpdatedAuthor());

        var argumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).findById(anyLong());
        verify(authorRepository).save(argumentCaptor.capture());
        Author captorValue = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(captorValue.getId()).isEqualTo(AUTHORS_DATA.getId());
            assertThat(captorValue.getName()).isEqualTo(AUTHORS_DATA.getUpdatedName());
        });
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void delete_shouldThrowsException_whenAuthorIsNotExist() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            authorService.delete(AUTHORS_DATA.getId());
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Entity with id: " + AUTHORS_DATA.getId() + " is not found");
        }

        verify(authorRepository).findById(anyLong());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void delete_shouldDoNothing_whenAuthorIsExist() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(AUTHORS_DATA.getAuthor()));

        authorService.delete(AUTHORS_DATA.getId());

        verify(authorRepository).findById(anyLong());
        verify(authorRepository).delete(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void getByNameContaining_shouldReturnPaginatedAuthors_whenIsInvoke() {
        when(authorRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(mock(Page.class));

        authorService.getByNameContaining(AUTHORS_DATA.getName(), Pageable.unpaged());

        verify(authorRepository).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void getAllBooksById() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(AUTHORS_DATA.getAuthor()));
        when(authorRepository.getBooksByEntity(any(Author.class), anyInt(), any(Pageable.class))).thenReturn(mock(Page.class));

        authorService.getAllBooksById(AUTHORS_DATA.getId(), 0, Pageable.unpaged());

        verify(authorRepository).findById(anyLong());
        verify(authorRepository).getBooksByEntity(any(Author.class), anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findAll_shouldReturnAllAuthors_whenIsInvoke() {
        when(authorRepository.findAll()).thenReturn(mock(List.class));

        authorService.findAll();

        verify(authorRepository).findAll();
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findOrSave_shouldSaveAuthor_whenAuthorIsNotExist() {
        when(authorRepository.findByName(anyString())).thenReturn(Optional.empty());

        authorService.findOrSave(AUTHORS_DATA.getNewAuthor());

        verify(authorRepository).findByName(AUTHORS_DATA.getName());
        verify(authorRepository).save(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findOrSave_shouldReturnAuthor_whenAuthorIsExist() {
        when(authorRepository.findByName(anyString())).thenReturn(Optional.of(AUTHORS_DATA.getAuthor()));

        authorService.findOrSave(AUTHORS_DATA.getNewAuthor());

        verify(authorRepository).findByName(AUTHORS_DATA.getName());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void restoreById_shouldDoNothing_whenMethodIsInvoke() {
        doNothing().when(authorRepository).restore(anyLong());

        authorService.restoreById(AUTHORS_DATA.getId());

        verify(authorRepository).restore(anyLong());
        verifyNoMoreInteractions(authorRepository);
    }
}