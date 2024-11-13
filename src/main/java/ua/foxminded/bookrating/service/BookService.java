package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.projection.BookRatingProjection;

import java.util.List;

public interface BookService extends CrudService<Book> {
    Page<BookRatingProjection> findAllPaginated(Integer desiredAverageRating, Pageable pageable);

    Book save(BookDto entity);

    Book update(Long id, BookDto entity);

    Book getByIsbn(String isbn);

    Page<BookRatingProjection> getByTitleContaining(String title, Pageable pageable);

    Page<BookRatingProjection> getBooksByAuthorAndPublisher(List<Long> authorsId, List<Long> publishersId, Integer desiredAverageRating, String title, Pageable pageable);
}
