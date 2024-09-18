package ua.foxminded.bookrating.springBatch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ua.foxminded.bookrating.dto.*;
import ua.foxminded.bookrating.persistance.entity.*;
import ua.foxminded.bookrating.springBatch.process.*;

@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step authorStep(FlatFileItemReader<AuthorCsvDto> reader,
                           AuthorItemProcess process,
                           ItemWriterAdapter<Author> writer) {
        return createStep(reader, process, writer, "authorStep");
    }

    @Bean
    public Step publisherStep(FlatFileItemReader<PublisherCsvDto> reader,
                              PublisherItemProcess process,
                              ItemWriterAdapter<Publisher> writer) {
        return createStep(reader, process, writer, "publisherStep");
    }

    @Bean
    public Step userStep(FlatFileItemReader<UserCsvDto> reader,
                         UserItemProcessor process,
                         ItemWriterAdapter<User> writer) {
        return createStep(reader, process, writer, "userStep");
    }

    @Bean
    public Step bookStep(FlatFileItemReader<BookCsvDto> reader,
                         BookItemProcess process,
                         ItemWriterAdapter<Book> writer) {
        return createStep(reader, process, writer, "bookStep");

    }

    @Bean
    public Step ratingStep(FlatFileItemReader<RatingCsvDto> reader,
                           RatingItemProcess process,
                           ItemWriterAdapter<Rating> writer) {
        return createStep(reader, process, writer, "ratingStep");
    }

    private <T, S> Step createStep(FlatFileItemReader<T> reader,
                                   ItemProcessor<T, S> process,
                                   ItemWriterAdapter<S> writer,
                                   String name) {
        return new StepBuilder(name, jobRepository)
                .<T, S>chunk(1000, transactionManager)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(100)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}
