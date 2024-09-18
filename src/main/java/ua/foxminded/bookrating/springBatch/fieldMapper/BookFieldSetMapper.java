package ua.foxminded.bookrating.springBatch.fieldMapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import ua.foxminded.bookrating.dto.BookCsvDto;

public class BookFieldSetMapper implements FieldSetMapper<BookCsvDto> {
    @Override
    public BookCsvDto mapFieldSet(FieldSet fieldSet) throws BindException {
        return new BookCsvDto(
                fieldSet.readString("isbn"),
                fieldSet.readString("title"),
                fieldSet.readString("author"),
                fieldSet.readString("publicationYear"),
                fieldSet.readString("publisher"),
                fieldSet.readString("imageUrlS"),
                fieldSet.readString("imageUrlM"),
                fieldSet.readString("imageUrlL")
        );
    }
}
