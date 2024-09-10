package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.service.PublisherService;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublisherItemStreamWriter implements ItemStreamWriter<Publisher> {

    private final PublisherService publisherService;

    @Override
    public void write(Chunk<? extends Publisher> chunk) {
        publisherService.saveAll(new HashSet<>(chunk.getItems()));
    }
}
