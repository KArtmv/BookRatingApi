package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.persistance.repo.UserRepository;
import ua.foxminded.bookrating.service.UserService;

@Service
public class UserServiceImpl extends CrudServiceImpl<User> implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public Page<Rating> findRatedBooksByUser(Long userId, Pageable pageable) {
        return userRepository.findByRatingsUser(findById(userId), pageable);
    }
}
