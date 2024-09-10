package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}