package ua.foxminded.bookrating.service.implementation;

import org.springframework.stereotype.Service;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.repo.AuthorRepository;
import ua.foxminded.bookrating.service.AuthorService;

import java.util.Set;

@Service
public class AuthorServiceImpl extends AbstractServiceImpl<Author> implements AuthorService {


    public AuthorServiceImpl(AuthorRepository authorRepository) {
        super(authorRepository);
    }

    @Override
    public void saveAll(Set<Author> authors) {
        repository.saveAll(authors);
    }
}
