package ua.foxminded.bookrating.springBatch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ua.foxminded.bookrating.dto.*;

import java.nio.charset.StandardCharsets;

@Configuration
public class Reader {

    public static final String DATASET_USERS_CSV = "dataset/users.csv";
    public static final String DATASET_BOOKS_CSV = "dataset/books.csv";
    public static final String DATASET_RATINGS_CSV = "dataset/ratings.csv";
    private static final String FILE_ENCODING = StandardCharsets.ISO_8859_1.name();

    @Bean
    public FlatFileItemReader<AuthorCsvDto> authorItemReader() {
        return new FlatFileItemReaderBuilder<AuthorCsvDto>()
                .name("authorItemReader")
                .resource(new ClassPathResource(DATASET_BOOKS_CSV))
                .linesToSkip(1)
                .encoding(FILE_ENCODING)
                .delimited().delimiter(";")
                .quoteCharacter('\"')
                .names("isbn", "title", "author", "publicationYear", "publisher", "imageUrlS", "imageUrlM", "imageUrlL")
                .fieldSetMapper(fieldSet -> new AuthorCsvDto(fieldSet.readString("author")))
                .build();
    }

    @Bean
    public FlatFileItemReader<PublisherCsvDto> publisherItemReader() {
        return new FlatFileItemReaderBuilder<PublisherCsvDto>()
                .name("publisherItemReader")
                .resource(new ClassPathResource(DATASET_BOOKS_CSV))
                .linesToSkip(1)
                .encoding(FILE_ENCODING)
                .delimited().delimiter(";")
                .quoteCharacter('\"')
                .names("isbn", "title", "author", "publicationYear", "publisher", "imageUrlS", "imageUrlM", "imageUrlL")
                .fieldSetMapper(fieldSet -> new PublisherCsvDto(fieldSet.readString("publisher")))
                .build();
    }

    @Bean
    public FlatFileItemReader<UserCsvDto> userItemReader() {
        return new FlatFileItemReaderBuilder<UserCsvDto>()
                .name("userItemReader")
                .resource(new ClassPathResource(DATASET_USERS_CSV))
                .encoding(FILE_ENCODING)
                .linesToSkip(1)
                .delimited().delimiter(";")
                .names("userId", "location", "age")
                .targetType(UserCsvDto.class)
                .build();
    }

    @Bean
    public FlatFileItemReader<BookCsvDto> bookItemReader() {
        return new FlatFileItemReaderBuilder<BookCsvDto>()
                .name("bookItemReader")
                .resource(new ClassPathResource(DATASET_BOOKS_CSV))
                .linesToSkip(1)
                .encoding(FILE_ENCODING)
                .delimited().delimiter(";")
                .quoteCharacter('\"')
                .names("isbn", "title", "author", "publicationYear", "publisher", "imageUrlS", "imageUrlM", "imageUrlL")
                .targetType(BookCsvDto.class)
                .build();
    }

    @Bean
    public FlatFileItemReader<RatingCsvDto> ratingItemReader() {
        return new FlatFileItemReaderBuilder<RatingCsvDto>()
                .name("ratingItemReader")
                .resource(new ClassPathResource(DATASET_RATINGS_CSV))
                .linesToSkip(1)
                .delimited().delimiter(";")
                .names("userId", "isbn", "bookRaring")
                .targetType(RatingCsvDto.class)
                .build();
    }
}
