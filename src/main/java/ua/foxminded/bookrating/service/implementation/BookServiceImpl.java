package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.repo.BookRepository;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.PublisherService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl extends RestoreServiceImpl<Book> implements BookService {

    private final BookRepository bookRepository;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    public BookServiceImpl(BookRepository repository, PublisherService publisherService, AuthorService authorService) {
        super(repository);
        this.bookRepository = repository;
        this.publisherService = publisherService;
        this.authorService = authorService;
    }

    @Override
    public Page<BookRatingProjection> findAllPaginated(Integer desiredAverageRating, Pageable pageable) {
        return bookRepository.findAllPaginated(desiredAverageRating, pageable);
    }

    @Transactional
    @Override
    public Book save(BookDto bookDto) {
        return bookRepository.save(toEntity(bookDto, new Book()));
    }

    @Transactional
    @Override
    public Book update(Long id, BookDto entity) {
        return bookRepository.save(toEntity(entity, findById(id)));
    }

    @Override
    public Book getByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("A book with the given ISBN: " + isbn + " is not found"));
    }

    @Override
    public Page<BookRatingProjection> getByTitleContaining(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Page<BookRatingProjection> getBooksByAuthorAndPublisher(List<Long> authorsId,
                                                                   List<Long> publisherId,
                                                                   Integer desiredAverageRating,
                                                                   String title,
                                                                   Pageable pageable) {
        return bookRepository.findByAuthorsOrPublisherIn(
                authorsId == null ? Collections.emptyList() : authorsId.stream().map(authorService::findById).toList(),
                publisherId == null ? Collections.emptyList() : publisherId.stream().map(publisherService::findById).toList(),
                desiredAverageRating,
                title.trim(),
                pageable);
    }

    @Override
    public Page<Rating> getRatingsByBookId(Long id, Pageable pageable) {
        return bookRepository.findBookRatings(findById(id), pageable);
    }

    private Book toEntity(BookDto bookDto, Book book) {
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setPublicationYear(bookDto.getPublicationYear().toString());
        book.setPublisher(publisherService.findOrSave(bookDto.getPublisher()));
        book.setAuthors(bookDto.getAuthors().stream().map(authorService::findOrSave).collect(Collectors.toSet()));
        book.setImage(bookDto.getImage());
        return book;
    }
}
