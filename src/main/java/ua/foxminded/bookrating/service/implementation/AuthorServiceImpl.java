package ua.foxminded.bookrating.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.repo.AuthorRepository;
import ua.foxminded.bookrating.service.AuthorService;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl extends ExtendedCrudServiceImpl<Author> implements AuthorService {

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        super(authorRepository);
    }
}
