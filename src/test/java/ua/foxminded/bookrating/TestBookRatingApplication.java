package ua.foxminded.bookrating;

import org.springframework.boot.SpringApplication;

public class TestBookRatingApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookRatingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
