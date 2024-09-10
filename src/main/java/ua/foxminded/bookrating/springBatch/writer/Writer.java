package ua.foxminded.bookrating.springBatch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.foxminded.bookrating.persistance.entity.*;
import ua.foxminded.bookrating.service.BookService;
import ua.foxminded.bookrating.service.RatingService;
import ua.foxminded.bookrating.service.UserService;

@Configuration
@RequiredArgsConstructor
public class Writer {

    private final UserService userService;
    private final BookService bookService;
    private final RatingService ratingService;

    @Bean
    public SynchronizedItemStreamWriter<Author> authorWriter(AuthorItemStreamWriter authorItemWriter) {
        SynchronizedItemStreamWriter<Author> writer = new SynchronizedItemStreamWriter<>();
        writer.setDelegate(authorItemWriter);
        return writer;
    }

    @Bean
    public SynchronizedItemStreamWriter<Publisher> publisherWriter(PublisherItemStreamWriter publisherItemWriter) {
        SynchronizedItemStreamWriter<Publisher> writer = new SynchronizedItemStreamWriter<>();
        writer.setDelegate(publisherItemWriter);
        return writer;
    }

    @Bean
    public ItemWriterAdapter<User> userWriter() {
        ItemWriterAdapter<User> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(userService);
        writer.setTargetMethod("save");
        return writer;
    }

    @Bean
    public ItemWriterAdapter<Book> bookWriter() {
        ItemWriterAdapter<Book> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(bookService);
        writer.setTargetMethod("save");
        return writer;
    }

    @Bean
    public ItemWriterAdapter<Rating> ratingWriter() {
        ItemWriterAdapter<Rating> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(ratingService);
        writer.setTargetMethod("save");
        return writer;
    }
}
