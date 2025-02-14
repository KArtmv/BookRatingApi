package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.dto.UserDto;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.persistance.repo.UserRepository;
import ua.foxminded.bookrating.service.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends RestoreServiceImpl<User, UserDto> implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public Page<Rating> findRatedBooksByUser(Long userId, Pageable pageable) {
        return userRepository.findByRatingsUser(findById(userId), pageable);
    }

    @Override
    public User create(UserDto dto) {
        return super.save(new User(dto.location(), dto.age()));
    }

    @Override
    @Transactional
    public User update(Long id, UserDto updateUser) {
        User user = findById(id);
        user.setLocation(updateUser.location());
        user.setAge(updateUser.age());
        return userRepository.save(user);
    }
}
