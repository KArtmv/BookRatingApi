package ua.foxminded.bookrating.util.user;

import lombok.Getter;
import ua.foxminded.bookrating.persistance.entity.User;

@Getter
public class UserData {
    private final Long id = 58792L;
    private final String location = "Fenton, Michigan, Usa";
    private final Integer age = 21;
    private final User user = new User(id, location, age);
    private final User newUser = new User("stockton, california, usa", 18);
    private final Long deletedUserId = 55187L;
    private final String updatedLocation = "Mariupol, Donecka, Ukraine";
    private final Integer updatedAge = 32;
    private final User updatedUser = new User(updatedLocation, updatedAge);

    private final String userRatedBooksHref = "http://localhost/api/v1/users/58792/rated-books";
    private final String selfHref = "http://localhost/api/v1/users/58792";
}
