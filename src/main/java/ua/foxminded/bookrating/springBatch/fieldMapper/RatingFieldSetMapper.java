package ua.foxminded.bookrating.springBatch.fieldMapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import ua.foxminded.bookrating.dto.RatingCsvDto;

public class RatingFieldSetMapper implements FieldSetMapper<RatingCsvDto> {
    @Override
    public RatingCsvDto mapFieldSet(FieldSet fieldSet) {
        return new RatingCsvDto(
                fieldSet.readLong("userId"),
                fieldSet.readString("isbn"),
                fieldSet.readInt("bookRaring")
        );
    }
}
