package ua.foxminded.bookrating.springBatch.fieldMapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import ua.foxminded.bookrating.dto.PublisherCsvDto;

public class PublisherFieldSetMapper implements FieldSetMapper<PublisherCsvDto> {
    @Override
    public PublisherCsvDto mapFieldSet(FieldSet fieldSet) throws BindException {
        return new PublisherCsvDto(fieldSet.readString("publisher"));
    }
}
