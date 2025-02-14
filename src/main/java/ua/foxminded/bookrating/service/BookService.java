package ua.foxminded.bookrating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;

import java.util.List;

public interface BookService extends PaginatedService<Book, BookDto> {
    Book getByIsbn(String isbn);

    Page<Book> getBooksWithFilters(String title, List<Long> authorIds, List<Long> publisherIds, Integer publicationYear, Integer averageRating, Pageable pageable);

    Page<Rating> getRatingsByBookId(Long id, Pageable pageable);

    Book getDeletedBooksByIsbn(String isbn);
}
