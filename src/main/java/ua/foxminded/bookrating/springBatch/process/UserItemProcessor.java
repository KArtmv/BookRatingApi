package ua.foxminded.bookrating.springBatch.process;

import org.apache.commons.text.WordUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.dto.UserCsvDto;
import ua.foxminded.bookrating.persistance.entity.User;

@Component
public class UserItemProcessor implements ItemProcessor<UserCsvDto, User> {

    @Override
    public User process(UserCsvDto item) {
        return new User(item.userId(), capitalize(item.location()), getAge(item.age()));
    }

    private String capitalize(String location) {
        return WordUtils.capitalizeFully(location);
    }

    private Integer getAge(String age) {
        if (age.equals("NULL")) {
            return 0;
        } else {
            return Integer.parseInt(age);
        }
    }
}
