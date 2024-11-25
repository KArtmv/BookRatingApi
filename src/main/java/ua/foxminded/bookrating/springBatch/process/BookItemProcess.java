package ua.foxminded.bookrating.springBatch.process;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.dto.BookCsvDto;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Image;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.service.PublisherService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookItemProcess implements ItemProcessor<BookCsvDto, Book>, StepExecutionListener {

    private Map<String, Author> authorCache;
    private Map<String, Publisher> publisherCache;

    private final AuthorService authorService;
    private final PublisherService publisherService;

    @Override
    public Book process(BookCsvDto item) {
        Publisher publisher = publisherCache.getOrDefault(replaceAmpersand(item.publisher().trim()), null);
        Author author = authorCache.getOrDefault(replaceAmpersand(item.author().trim()), null);

        if (author != null && publisher != null && item.title().length() < 255) {
            return toBook(item, publisher, author);
        } else {
            return null;
        }
    }

    private Book toBook(BookCsvDto item, Publisher publisher, Author author) {
        Book book = new Book();
        book.setIsbn(item.isbn().trim());
        book.setTitle(replaceAmpersand(item.title()));
        book.setPublisher(publisher);
        book.setPublicationYear(item.publicationYear());
        book.addAuthor(author);
        book.setImage(new Image(item.imageUrlS(), item.imageUrlM(), item.imageUrlL()));
        return book;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        authorCache = authorService.findAll().stream().collect(Collectors.toMap(Author::getName, Function.identity()));
        publisherCache = publisherService.findAll().stream().collect(Collectors.toMap(Publisher::getName, Function.identity()));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        authorCache.clear();
        publisherCache.clear();
        return ExitStatus.COMPLETED;
    }

    private String replaceAmpersand(String string) {
        return string.replace("&amp;", "&").trim();
    }
}
