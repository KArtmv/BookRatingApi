package ua.foxminded.bookrating.springBatch.process;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.dto.BookCsvDto;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Image;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.service.PublisherService;

import java.time.Year;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookItemProcess implements ItemProcessor<BookCsvDto, Book>, StepExecutionListener {

    private Map<String, Author> authorCache;
    private Map<String, Publisher> publisherCache;
    private Set<String> bookIsbnCache;

    private final AuthorService authorService;
    private final PublisherService publisherService;

    @Override
    public Book process(BookCsvDto item) {
        return bookIsbnCache.contains(item.isbn()) ? null : getBook(item, getAuthor(item.author()), getPublisher(item.publisher()));
    }

    private Book getBook(BookCsvDto item, Author author, Publisher publisher) {
        if (author != null && publisher != null && item.title().length() < 255) {
            bookIsbnCache.add(item.isbn());
            return new Book(item.isbn(), item.title(), Year.parse(item.publicationYear()), publisher, Set.of(author), new Image(item.imageUrlS(), item.imageUrlM(), item.imageUrlL()));
        } else {
            return null;
        }
    }

    private Author getAuthor(String authorName) {
        return authorCache.getOrDefault(replaceAmpersand(authorName.trim()), null);
    }

    private Publisher getPublisher(String publisherName) {
        return publisherCache.getOrDefault(replaceAmpersand(publisherName.trim()), null);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        bookIsbnCache = new HashSet<>();
        authorCache = authorService.findAll().stream().collect(Collectors.toMap(Author::getName, Function.identity()));
        publisherCache = publisherService.findAll().stream().collect(Collectors.toMap(Publisher::getName, Function.identity()));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        authorCache.clear();
        publisherCache.clear();
        bookIsbnCache.clear();
        return ExitStatus.COMPLETED;
    }

    private String replaceAmpersand(String string) {
        return string.replace("&amp;", "&").trim();
    }
}
