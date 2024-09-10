package ua.foxminded.bookrating.springBatch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;
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
                           SynchronizedItemStreamWriter<Author> writer) {
        return new StepBuilder("authorStep", jobRepository)
                .<AuthorCsvDto, Author>chunk(1000, transactionManager)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skip(DataIntegrityViolationException.class)
                .skipLimit(Integer.MAX_VALUE)
                .taskExecutor(taskExecutor())
                .listener(process)
                .build();
    }

    @Bean
    public Step publisherStep(FlatFileItemReader<PublisherCsvDto> reader,
                              PublisherItemProcess process,
                              SynchronizedItemStreamWriter<Publisher> writer) {
        return new StepBuilder("publisherStep", jobRepository)
                .<PublisherCsvDto, Publisher>chunk(1000, transactionManager)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skip(DataIntegrityViolationException.class)
                .skipLimit(Integer.MAX_VALUE)
                .taskExecutor(taskExecutor())
                .listener(process)
                .build();
    }

    @Bean
    public Step userStep(FlatFileItemReader<UserRawDataDto> reader,
                         UserItemProcessor process,
                         ItemWriterAdapter<User> writer) {
        return new StepBuilder("userStep", jobRepository)
                .<UserRawDataDto, User>chunk(1000, transactionManager)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .faultTolerant()
                .skipLimit(20)
                .skip(FlatFileParseException.class)
                .build();
    }

    @Bean
    public Step bookStep(FlatFileItemReader<BookRawDataDto> reader,
                         BookItemProcess process,
                         ItemWriterAdapter<Book> writer) {
        return new StepBuilder("bookStep", jobRepository)
                .<BookRawDataDto, Book>chunk(1000, transactionManager)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .faultTolerant()
                .skipLimit(100)
                .skip(FlatFileParseException.class)
                .skip(DataIntegrityViolationException.class)
                .taskExecutor(taskExecutor())
                .listener(process)
                .build();
    }

    @Bean
    public Step ratingStep(FlatFileItemReader<RatingRawDataDto> reader,
                           RatingItemProcess process,
                           ItemWriterAdapter<Rating> writer) {
        return new StepBuilder("ratingStep", jobRepository)
                .<RatingRawDataDto, Rating>chunk(1000, transactionManager)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .faultTolerant()
                .skipLimit(100)
                .skip(FlatFileParseException.class)
                .taskExecutor(taskExecutor())
                .listener(process)
                .build();
    }

    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("asyncTaskExecutor");
    }
}
