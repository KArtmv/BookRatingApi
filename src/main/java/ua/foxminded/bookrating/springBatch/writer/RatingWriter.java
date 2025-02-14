package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.persistance.entity.Rating;
import ua.foxminded.bookrating.persistance.repo.RatingRepository;
import ua.foxminded.bookrating.service.RatingService;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class RatingWriter implements ItemWriter<Rating> {

    private final RatingRepository ratingRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void write(Chunk<? extends Rating> chunk) throws ExecutionException, InterruptedException {
        Future<List<?>> ratingFuture = executorService.submit(() -> ratingRepository.saveAll(chunk.getItems()));
        ratingFuture.get();
    }
}
