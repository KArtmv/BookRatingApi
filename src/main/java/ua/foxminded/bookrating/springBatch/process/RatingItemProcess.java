package ua.foxminded.bookrating.springBatch.process;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.dto.RatingRawDataDto;
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
public class RatingItemProcess implements ItemProcessor<RatingRawDataDto, Rating>, StepExecutionListener {

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
    public Rating process(RatingRawDataDto item) {
        Book book = bookCache.getOrDefault(item.isbn(), null);
        User user = userCache.getOrDefault(item.userId(), null);

        if (book != null && user != null) {
            Rating rating = new Rating();
            rating.setBook(book);
            rating.setUser(user);
            rating.setBookRating(item.bookRaring());
            return rating;
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
