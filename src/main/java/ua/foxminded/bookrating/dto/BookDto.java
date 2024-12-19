package ua.foxminded.bookrating.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import ua.foxminded.bookrating.annotation.Isbn;
import ua.foxminded.bookrating.persistance.entity.Image;

import java.time.Year;
import java.util.List;

@Getter
@Setter
@Isbn(bookId = "id", isbn = "isbn", message = "The provided ISBN already exists for another book.")
public class BookDto {
    private Long id;

    @NotBlank(message = "The ISBN of the book is required and cannot be blank.")
    @ISBN(type = ISBN.Type.ANY, message = "The provided ISBN is not valid.")
    private String isbn;

    @NotBlank(message = "The title of the book is required and cannot be empty.")
    private String title;

    @NotNull(message = "The publication year is required and cannot be null.")
    @PastOrPresent(message = "The publication year must be in the past or the present.")
    private Year publicationYear;

    @NotEmpty(message = "At least one author is required for the book.")
    private List<Long> authorsId;

    @NotNull(message = "The publisher is required and cannot be null.")
    private Long publisherId;

    @Valid
    private Image image;
}
