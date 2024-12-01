package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.repo.BookRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookWriter implements ItemStreamWriter<Book> {

    private static final Set<Book> BOOKS = new HashSet<>();

    private final BookRepository bookRepository;

    @Override
    public void write(Chunk<? extends Book> chunk) throws Exception {
        log.debug("run write method");
        BOOKS.addAll(chunk.getItems());
    }

    @Override
    public void close() throws ItemStreamException {
        log.debug("run close method");
        log.debug("Size of the BOOKS before saving: {}", BOOKS.size());
        bookRepository.saveAllAndFlush(BOOKS);
        BOOKS.clear();
        log.debug("Size of the BOOKS after cleaning: {}", BOOKS.size());
    }
}
