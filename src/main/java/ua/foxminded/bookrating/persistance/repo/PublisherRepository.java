package ua.foxminded.bookrating.persistance.repo;

import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Publisher;

@Repository
public interface PublisherRepository extends ExtendedRepository<Publisher, Long> {
}