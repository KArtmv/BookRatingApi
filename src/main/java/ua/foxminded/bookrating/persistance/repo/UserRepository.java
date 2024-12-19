package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u.ratings from User u join u.ratings ratings where ratings.user = :user")
    Page<Rating> findByRatingsUser(@Param("user") User user, Pageable pageable);
}