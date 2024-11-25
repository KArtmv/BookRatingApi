package ua.foxminded.bookrating.dto;

import lombok.Getter;
import lombok.Setter;
import ua.foxminded.bookrating.persistance.entity.Image;

import java.util.List;

@Getter
@Setter
public class BookDto {
    private String isbn;
    private String title;
    private String publicationYear;
    private List<Long> authorsId;
    private Long publisherId;
    private Image image;
}
