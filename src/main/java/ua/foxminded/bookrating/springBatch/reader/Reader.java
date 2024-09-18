package ua.foxminded.bookrating.springBatch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ua.foxminded.bookrating.dto.*;
import ua.foxminded.bookrating.springBatch.fieldMapper.*;

import java.nio.charset.StandardCharsets;

@Configuration
public class Reader {

    public static final String USERS_DATASET_CSV = "dataset/users.csv";
    public static final String BOOKS_DATASET_CSV = "dataset/books.csv";
    public static final String RATINGS_DATASET_CSV = "dataset/ratings.csv";

    private static final String[] BOOK_COLUMN_NAMES = {"isbn","title","author","publicationYear","publisher","imageUrlS","imageUrlM","imageUrlL"};
    private static final String[] RATING_COLUMN_NAMES = {"userId", "isbn", "bookRaring"};
    private static final String[] USER_COLUMN_NAMES = {"userId", "location", "age"};

    @Bean
    public FlatFileItemReader<AuthorCsvDto> authorItemReader() {
        return createCsvReader(BOOKS_DATASET_CSV, BOOK_COLUMN_NAMES, new AuthorFieldSetMapper(), "authorItemReader");
    }

    @Bean
    public FlatFileItemReader<PublisherCsvDto> publisherItemReader() {
        return createCsvReader(BOOKS_DATASET_CSV, BOOK_COLUMN_NAMES, new PublisherFieldSetMapper(), "publisherItemReader");
    }

    @Bean
    public FlatFileItemReader<UserCsvDto> userItemReader() {
        return createCsvReader(USERS_DATASET_CSV, USER_COLUMN_NAMES, new UserFieldSetMapper(), "userItemReader");
    }

    @Bean
    public FlatFileItemReader<BookCsvDto> bookItemReader() {
        return createCsvReader(BOOKS_DATASET_CSV, BOOK_COLUMN_NAMES, new BookFieldSetMapper(), "bookItemReader");
    }

    @Bean
    public FlatFileItemReader<RatingCsvDto> ratingItemReader() {
        return createCsvReader(RATINGS_DATASET_CSV, RATING_COLUMN_NAMES, new RatingFieldSetMapper(), "ratingItemReader");
    }

    private <T> FlatFileItemReader<T> createCsvReader(String filename, String[] filedNames, FieldSetMapper<T> fieldSetMapper, String name) {
        return new FlatFileItemReaderBuilder<T>()
                .name(name)
                .resource(new ClassPathResource(filename))
                .encoding(StandardCharsets.ISO_8859_1.name())
                .linesToSkip(1)
                .delimited().delimiter(";")
                .quoteCharacter('\"')
                .names(filedNames)
                .fieldSetMapper(fieldSetMapper)
                .build();
    }
}
