package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.repo.AuthorRepository;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.AuthorService;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl extends ExtendedCrudServiceImpl<Author> implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        super(authorRepository);
        this.authorRepository = authorRepository;
    }

    @Override
    public Page<BookRatingProjection> getAllBooksById(Long authorId, Integer desiredAverageRating, Pageable pageRequest) {
        return authorRepository.getBooksByAuthor(findById(authorId), desiredAverageRating, pageRequest);
    }

    @Override
    @Transactional
    public Author save(Author author) {
        if (!super.existsByName(author.getName())) {
            return authorRepository.save(author);
        }
        throw new EntityExistsException("Author with name " + author.getName() + " already exists");
    }

    @Override
    @Transactional
    public Author update(Long id, Author newAuthor) {
        Author author = findById(id);
        author.setName(newAuthor.getName());
        return authorRepository.save(author);
    }
}
