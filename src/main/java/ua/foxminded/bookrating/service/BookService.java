package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.dto.BookFilterRequest;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.projection.BookRatingProjection;

import java.util.List;

public interface BookService extends PaginatedService<Book, BookDto> {
    Book getByIsbn(String isbn);

    Page<Book> getBooksWithFilters(String title, List<Long> authorIds, List<Long> publisherIds, Integer publicationTear, Integer averageRating, Pageable pageable);

    Page<Rating> getRatingsByBookId(Long id, Pageable pageable);
}
