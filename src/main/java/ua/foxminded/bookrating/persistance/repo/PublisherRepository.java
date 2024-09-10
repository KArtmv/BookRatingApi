package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Publisher;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO publisher (name) VALUES (:name) ON CONFLICT (name) DO NOTHING", nativeQuery = true)
//    Long saveIfNotExists(String name);
}