package ua.foxminded.bookrating.springBatch.fieldMapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import ua.foxminded.bookrating.dto.UserCsvDto;

public class UserFieldSetMapper implements FieldSetMapper<UserCsvDto> {
    @Override
    public UserCsvDto mapFieldSet(FieldSet fieldSet) {
        return new UserCsvDto(
                fieldSet.readLong("userId"),
                fieldSet.readString("location"),
                fieldSet.readString("age")
        );
    }
}
