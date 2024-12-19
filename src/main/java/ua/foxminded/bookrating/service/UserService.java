package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;

public interface UserService extends CrudService<User> {
    Page<Rating> findRatedBooksByUser(Long userId, Pageable pageable);
}
