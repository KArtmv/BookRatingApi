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
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.persistance.repo.PublisherRepository;
import ua.foxminded.bookrating.service.PublisherService;
import ua.foxminded.bookrating.util.publisher.PublisherData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PublisherServiceImpl.class)
class PublisherServiceImplTest {

    public static final PublisherData PUBLISHER_DATA = new PublisherData();
    @MockBean
    private PublisherRepository publisherRepository;
    @Autowired
    private PublisherService publisherService;

    @Test
    void findAllPaginated_shouldReturnAllAuthors() {
        when(publisherRepository.findAll(any(Pageable.class))).thenReturn(mock(Page.class));

        publisherService.findAll(Pageable.unpaged());

        verify(publisherRepository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void save_shouldThrowsException_whenAuthorIsExist() {
        when(publisherRepository.findByName(anyString())).thenReturn(Optional.of(PUBLISHER_DATA.getPublisher()));

        try {
            publisherService.save(PUBLISHER_DATA.getPublisher());
        } catch (EntityExistsException e) {
            assertThat(e.getMessage()).isEqualTo(PUBLISHER_DATA.getPublisher().getName() + " already exists");
        }
        verify(publisherRepository).findByName(anyString());
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void save_shouldSavedAuthor_whenAuthorIsNotExist() {
        when(publisherRepository.findByName(anyString())).thenReturn(Optional.empty());

        publisherService.save(PUBLISHER_DATA.getPublisher());

        verify(publisherRepository).findByName(anyString());
        verify(publisherRepository).save(any(Publisher.class));
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void update_shouldThrowsException_whenAuthorIsNotExist() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            publisherService.update(PUBLISHER_DATA.getId(), PUBLISHER_DATA.getNewPublisher());
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Entity with id: " + PUBLISHER_DATA.getId() + " is not found");
        }

        verify(publisherRepository).findById(anyLong());
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void update_shouldReturnUpdatedAuthor_whenAuthorIsExist() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(PUBLISHER_DATA.getPublisher()));

        publisherService.update(PUBLISHER_DATA.getId(), PUBLISHER_DATA.getUpdatedPublisher());

        var argumentCaptor = ArgumentCaptor.forClass(Publisher.class);
        verify(publisherRepository).findById(anyLong());
        verify(publisherRepository).save(argumentCaptor.capture());
        var captorValue = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(captorValue.getId()).isEqualTo(PUBLISHER_DATA.getId());
            assertThat(captorValue.getName()).isEqualTo(PUBLISHER_DATA.getUpdatedName());
        });
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void delete_shouldThrowsException_whenAuthorIsNotExist() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            publisherService.delete(PUBLISHER_DATA.getId());
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Entity with id: " + PUBLISHER_DATA.getId() + " is not found");
        }

        verify(publisherRepository).findById(anyLong());
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void delete_shouldDoNothing_whenAuthorIsExist() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(PUBLISHER_DATA.getPublisher()));

        publisherService.delete(PUBLISHER_DATA.getId());

        verify(publisherRepository).findById(anyLong());
        verify(publisherRepository).delete(any(Publisher.class));
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void getByNameContaining_shouldReturnPaginatedAuthors_whenIsInvoke() {
        when(publisherRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(mock(Page.class));

        publisherService.getByNameContaining(PUBLISHER_DATA.getName(), Pageable.unpaged());

        verify(publisherRepository).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void getAllBooksById() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(PUBLISHER_DATA.getPublisher()));
        when(publisherRepository.getBooksByEntity(any(Publisher.class), any(Pageable.class))).thenReturn(mock(Page.class));

        publisherService.getAllBooksById(PUBLISHER_DATA.getId(), Pageable.unpaged());

        verify(publisherRepository).findById(anyLong());
        verify(publisherRepository).getBooksByEntity(any(Publisher.class), any(Pageable.class));
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void findAll_shouldReturnAllAuthors_whenIsInvoke() {
        when(publisherRepository.findAll()).thenReturn(mock(List.class));

        publisherService.findAll();

        verify(publisherRepository).findAll();
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void findOrSave_shouldSaveAuthor_whenAuthorIsNotExist() {
        when(publisherRepository.findByName(anyString())).thenReturn(Optional.empty());

        publisherService.findOrSave(PUBLISHER_DATA.getNewPublisher());

        verify(publisherRepository).findByName(PUBLISHER_DATA.getName());
        verify(publisherRepository).save(any(Publisher.class));
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void findOrSave_shouldReturnAuthor_whenAuthorIsExist() {
        when(publisherRepository.findByName(anyString())).thenReturn(Optional.of(PUBLISHER_DATA.getPublisher()));

        publisherService.findOrSave(PUBLISHER_DATA.getNewPublisher());

        verify(publisherRepository).findByName(PUBLISHER_DATA.getName());
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    void restoreById_shouldDoNothing_whenMethodIsInvoke() {
        doNothing().when(publisherRepository).restore(anyLong());

        publisherService.restoreById(PUBLISHER_DATA.getId());

        verify(publisherRepository).restore(anyLong());
        verifyNoMoreInteractions(publisherRepository);
    }
}