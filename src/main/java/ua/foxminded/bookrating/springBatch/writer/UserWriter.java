package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.persistance.entity.User;
import ua.foxminded.bookrating.persistance.repo.UserRepository;
import ua.foxminded.bookrating.service.UserService;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserWriter implements ItemWriter<User> {

    private final UserRepository userRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void write(Chunk<? extends User> chunk) throws ExecutionException, InterruptedException {
        Future<List<?>> future = executorService.submit(() -> userRepository.saveAll(chunk.getItems()));
        future.get();
    }
}
