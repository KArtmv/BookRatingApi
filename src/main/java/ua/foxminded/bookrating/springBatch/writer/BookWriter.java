package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.repo.BookRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class BookWriter implements ItemWriter<Book> {

    private final BookRepository bookRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void write(Chunk<? extends Book> chunk) throws ExecutionException, InterruptedException {
        Future<List<?>> future = executorService.submit(() -> bookRepository.saveAll(chunk.getItems()));
        future.get();
    }
}
