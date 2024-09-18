package ua.foxminded.bookrating.springBatch.fieldMapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import ua.foxminded.bookrating.dto.AuthorCsvDto;

public class AuthorFieldSetMapper implements FieldSetMapper<AuthorCsvDto> {
    @Override
    public AuthorCsvDto mapFieldSet(FieldSet fieldSet) throws BindException {
        return new AuthorCsvDto(fieldSet.readString("author"));
    }
}
