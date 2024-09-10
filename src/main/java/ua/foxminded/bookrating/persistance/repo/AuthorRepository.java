package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO author (name) VALUES (:name) ON CONFLICT (name) DO NOTHING", nativeQuery = true)
//    Long saveIfNotExists(String name);
}