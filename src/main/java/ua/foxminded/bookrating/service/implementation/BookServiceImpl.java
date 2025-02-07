package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.dto.BookDto;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.repo.BookRepository;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.PublisherService;
import ua.foxminded.bookrating.specification.BookSpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl extends PaginatedServiceImpl<Book> implements BookService {

    private final BookRepository bookRepository;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    public BookServiceImpl(BookRepository repository, PublisherService publisherService, AuthorService authorService) {
        super(repository);
        this.bookRepository = repository;
        this.publisherService = publisherService;
        this.authorService = authorService;
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
    public Page<Book> getBooksWithFilters(String title, List<Long> authorIds, List<Long> publisherIds,
                                          Integer publicationYear, Integer averageRating, Pageable pageable) {
        Specification<Book> specification = Specification.where(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasPublicationYear(publicationYear))
                .and(BookSpecification.hasAverageRating(averageRating));
        specification = filterByAuthors(authorIds, specification);
        specification = filterByPublishers(publisherIds, specification);
        return bookRepository.findAll(specification, pageable);
    }

    @Override
    public Page<Rating> getRatingsByBookId(Long id, Pageable pageable) {
        return bookRepository.findBookRatings(findById(id), pageable);
    }

    private Book toEntity(BookDto bookDto, Book book) {
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setPublisher(publisherService.findOrSave(bookDto.getPublisher()));
        book.setAuthors(bookDto.getAuthors().stream().map(authorService::findOrSave).collect(Collectors.toSet()));
        book.setImage(bookDto.getImage());
        return book;
    }

    private Specification<Book> filterByAuthors(List<Long> authorIds, Specification<Book> specification) {
        if (authorIds != null) {
            specification = specification.and(BookSpecification.hasAuthors(authorIds.stream().map(authorService::findById).toList()));
        }
        return specification;
    }

    private Specification<Book> filterByPublishers(List<Long> publisherIds, Specification<Book> specification) {
        if (publisherIds != null) {
            specification = specification.and(BookSpecification.hasPublishers(publisherIds.stream().map(publisherService::findById).toList()));
        }
        return specification;
    }
}
