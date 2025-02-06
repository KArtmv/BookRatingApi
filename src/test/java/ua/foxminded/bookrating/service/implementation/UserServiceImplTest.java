package ua.foxminded.bookrating.service.implementation;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.persistance.repo.UserRepository;
import ua.foxminded.bookrating.service.UserService;
import ua.foxminded.bookrating.util.user.UserData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {
    public static final UserData USER_DATA = new UserData();

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void findRatedBooksByUserId_shouldReturnPagedResult_whenInvoke() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_DATA.getUser()));
        when(userRepository.findByRatingsUser(any(User.class), any(Pageable.class))).thenReturn(mock(Page.class));

        userService.findRatedBooksByUser(USER_DATA.getId(), Pageable.unpaged());

        verify(userRepository).findById(anyLong());
        verify(userRepository).findByRatingsUser(any(User.class), any(Pageable.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAll_shouldReturnAllUsers_whenInvoke() {
        when(userRepository.findAll()).thenReturn(mock(List.class));

        userService.findAll();

        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findById_shouldReturnUser_whenInvoke() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_DATA.getUser()));

        userService.findById(USER_DATA.getId());

        verify(userRepository).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void update_shouldUpdateUser_whenInvoke() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_DATA.getUser()));

        userService.update(USER_DATA.getId(), USER_DATA.getUpdatedUser());

        verify(userRepository).findById(USER_DATA.getId());
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());
        assertAll(() -> {
            User captorValue = argumentCaptor.getValue();
            assertThat(captorValue.getId()).isEqualTo(USER_DATA.getId());
            assertThat(captorValue.getLocation()).isEqualTo(USER_DATA.getUpdatedLocation());
            assertThat(captorValue.getAge()).isEqualTo(USER_DATA.getUpdatedAge());
        });
    }
}