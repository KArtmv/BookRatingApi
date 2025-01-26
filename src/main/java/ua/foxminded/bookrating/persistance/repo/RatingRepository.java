package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Modifying
    @Query(value = "delete from rating r where r.id = :#{#rating.id}", nativeQuery = true)
    void delete(@Param("rating") Rating rating);
}