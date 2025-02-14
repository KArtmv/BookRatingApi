package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.dto.AuthorDto;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.repo.AuthorRepository;
import ua.foxminded.bookrating.service.AuthorService;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl extends ExtendedCrudServiceImpl<Author, AuthorDto> implements AuthorService {

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        super(authorRepository, Author::new);
    }

    @Override
    public Author create(AuthorDto dto) {
        if (findByName(dto.name()).isPresent()) {
            throw new EntityExistsException("Author with given name: " + dto.name() + ", already exists");
        }
        return super.save(new Author(dto.name()));
    }

    @Override
    public Author update(Long id, AuthorDto dto) {
        Author author = findById(id);
        author.setName(dto.name());
        return super.save(author);
    }
}
