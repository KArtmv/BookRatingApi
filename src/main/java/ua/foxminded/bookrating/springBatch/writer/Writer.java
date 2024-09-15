package ua.foxminded.bookrating.springBatch.writer;

import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.foxminded.bookrating.persistance.entity.*;
import ua.foxminded.bookrating.service.*;

@Configuration
public class Writer {

    @Bean
    public ItemWriterAdapter<User> userWriter(UserService userService) {
        return createWriterAdapter(userService);
    }

    @Bean
    public ItemWriterAdapter<Book> bookWriter(BookService bookService) {
        return createWriterAdapter(bookService);
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

    private <T, S> ItemWriterAdapter<T> createWriterAdapter(S service) {
        ItemWriterAdapter<T> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(service);
        writer.setTargetMethod("save");
        return writer;
    }
}
