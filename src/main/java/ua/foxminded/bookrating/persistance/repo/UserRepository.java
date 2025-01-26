package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    @Query("select u.ratings from User u join u.ratings ratings where ratings.user = :user")
    Page<Rating> findByRatingsUser(@Param("user") User user, Pageable pageable);

    @Override
    @Modifying
    @Query(value = "update users s set deleted = false where s.id = :id", nativeQuery = true)
    void restore(Long id);
}