package ua.foxminded.bookrating.persistance.repo;

import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Author;

@Repository
public interface AuthorRepository extends ExtendedRepository<Author, Long> {
}