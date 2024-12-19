package ua.foxminded.bookrating.springBatch.writer;

import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.foxminded.bookrating.persistance.entity.*;
import ua.foxminded.bookrating.service.AuthorService;
import ua.foxminded.bookrating.service.PublisherService;
import ua.foxminded.bookrating.service.RatingService;
import ua.foxminded.bookrating.service.UserService;

@Configuration
public class Writer {

    @Bean
    public ItemWriterAdapter<User> userWriter(UserService userService) {
        return createWriterAdapter(userService);
    }

    @Bean
    public ItemWriterAdapter<Rating> ratingWriter(RatingService ratingService) {
        return createWriterAdapter(ratingService);
    }

    @Bean
    public ItemWriterAdapter<Author> authorWriter(AuthorService authorService) {
        return createWriterAdapter(authorService);
    }

    @Bean
    public ItemWriterAdapter<Publisher> publisherWriter(PublisherService publisherService) {
        return createWriterAdapter(publisherService);
    }

    @Bean
    public SynchronizedItemStreamWriter<Book> syncBookWriter(BookWriter bookWriter) {
        SynchronizedItemStreamWriter<Book> writer = new SynchronizedItemStreamWriter<>();
        writer.setDelegate(bookWriter);
        return writer;
    }

    private <T, S> ItemWriterAdapter<T> createWriterAdapter(S service) {
        ItemWriterAdapter<T> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(service);
        writer.setTargetMethod("save");
        return writer;
    }
}
