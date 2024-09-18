package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.service.BookService;

@Service
public class BookServiceImpl extends AbstractServiceImpl<Book> implements BookService {

    public BookServiceImpl(JpaRepository<Book, Long> repository) {
        super(repository);
    }
}
