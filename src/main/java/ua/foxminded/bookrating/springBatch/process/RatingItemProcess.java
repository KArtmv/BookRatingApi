package ua.foxminded.bookrating.springBatch.process;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.dto.RatingCsvDto;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.UserService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RatingItemProcess implements ItemProcessor<RatingCsvDto, Rating>, StepExecutionListener {

    private Map<String, Book> bookCache;
    private Map<Long, User> userCache;

    private final BookService bookService;
    private final UserService userService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        bookCache = bookService.findAll().stream().collect(Collectors.toMap(Book::getIsbn, Function.identity()));
        userCache = userService.findAll().stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }

    @Override
    public Rating process(RatingCsvDto item) {
        Book book = bookCache.getOrDefault(item.isbn(), null);
        User user = userCache.getOrDefault(item.userId(), null);

        if (book != null && user != null) {
            return new Rating(book, user, item.bookRaring());
        } else {
            return null;
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        bookCache.clear();
        userCache.clear();
        return ExitStatus.COMPLETED;
    }
}
