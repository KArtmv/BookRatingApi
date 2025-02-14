package ua.foxminded.bookrating.springBatch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ua.foxminded.bookrating.dto.*;
import ua.foxminded.bookrating.persistance.entity.*;
import ua.foxminded.bookrating.springBatch.process.*;
import ua.foxminded.bookrating.springBatch.writer.*;

@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step authorStep(FlatFileItemReader<AuthorCsvDto> reader,
                           AuthorItemProcess process,
                           AuthorWriter writer) {
        return createStep(reader, process, writer, "authorStep");
    }

    @Bean
    public Step publisherStep(FlatFileItemReader<PublisherCsvDto> reader,
                              PublisherItemProcess process,
                              PublisherWriter writer) {
        return createStep(reader, process, writer, "publisherStep");
    }

    @Bean
    public Step userStep(FlatFileItemReader<UserCsvDto> reader,
                         UserItemProcessor process,
                         UserWriter writer) {
        return createStep(reader, process, writer, "userStep");
    }

    @Bean
    public Step bookStep(FlatFileItemReader<BookCsvDto> reader,
                         BookItemProcess process,
                         BookWriter writer) {
        return createStep(reader, process, writer, "bookStep");

    }

    @Bean
    public Step ratingStep(FlatFileItemReader<RatingCsvDto> reader,
                           RatingItemProcess process,
                           RatingWriter writer) {
        return createStep(reader, process, writer, "ratingStep");
    }

    private <T, S> Step createStep(FlatFileItemReader<T> reader,
                                   ItemProcessor<T, S> process,
                                   ItemWriter<S> writer,
                                   String name) {
        return new StepBuilder(name, jobRepository)
                .<T, S>chunk(10000, transactionManager)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(100)
                .build();
    }
}
