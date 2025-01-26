package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private static final Set<String> BOOKS_ISBN = new HashSet<>();

    private final BookRepository bookRepository;

    @Override
    public void write(Chunk<? extends Book> chunk) {
        log.debug("Processing chunk with {} books.", chunk.getItems().size());
        Set<Book> books = new HashSet<>(chunk.getItems());
        books.stream().filter(book -> !BOOKS_ISBN.contains(book.getIsbn()))
                .forEach(book -> BOOKS_ISBN.add(bookRepository.save(book).getIsbn()));
        log.debug("{} new books were successfully saved in this chunk.", books.size());
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("Closing writer. Total unique books processed: {}. Clearing state.", BOOKS_ISBN.size());
        BOOKS_ISBN.clear();
    }
}
