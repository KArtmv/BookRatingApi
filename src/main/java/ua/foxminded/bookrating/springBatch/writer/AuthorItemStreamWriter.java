package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.service.AuthorService;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorItemStreamWriter implements ItemStreamWriter<Author> {

    private final AuthorService authorService;

    @Override
    public void write(Chunk<? extends Author> chunk) {
        authorService.saveAll(new HashSet<>(chunk.getItems()));
    }
}
