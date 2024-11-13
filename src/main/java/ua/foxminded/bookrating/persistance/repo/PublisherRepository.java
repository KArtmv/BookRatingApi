package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.projection.BookRatingProjection;

@Repository
public interface PublisherRepository extends BaseRepository<Publisher, Long> {
}