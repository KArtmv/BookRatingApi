package ua.foxminded.bookrating.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;
import ua.foxminded.bookrating.annotation.Isbn;
import ua.foxminded.bookrating.persistance.entity.Image;

import java.time.Year;
import java.util.List;

@Getter
@Setter
@Isbn(bookId = "id", isbn = "isbn", message = "ISBN already exists for another book.")
public class BookDto {
    private Long id;
    @NotBlank(message = "The ISBN of book is required")
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;
    @NotBlank(message = "The title of the book is required and cannot be empty")
    private String title;
    @PastOrPresent
    private Year publicationYear;
    private List<Long> authorsId;
    private Long publisherId;
    @Valid
    private Image image;
}
