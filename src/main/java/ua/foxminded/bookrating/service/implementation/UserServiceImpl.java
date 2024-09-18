package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.service.UserService;

@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

    public UserServiceImpl(JpaRepository<User, Long> repository) {
        super(repository);
    }
}
