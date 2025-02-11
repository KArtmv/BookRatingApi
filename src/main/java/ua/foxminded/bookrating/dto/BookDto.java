package ua.foxminded.bookrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.lang.Nullable;
import ua.foxminded.bookrating.annotation.Isbn;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Image;

import java.time.Year;
import java.util.List;

/**
 * DTO for {@link Book}
 */
@Getter
@Setter
@Isbn(bookId = "id", isbn = "isbn", message = "The provided ISBN already exists for another book.")
@Schema(description = "Data transfer object for creating or updating book")
public class BookDto {
    @Schema(description = """
                Book id used for validation.
                If provided, it is used to verify whether the given ISBN belongs to the same book or another existing book
            """, hidden = true)
    private Long id;

    @Schema(description = "ISBN of the book. Must be unique across all books.", example = "0736688390")
    @NotBlank(message = "The ISBN of the book is required and cannot be blank.")
    @ISBN(type = ISBN.Type.ANY, message = "The provided ISBN is not valid.")
    private String isbn;

    @Schema(description = "Title of the book", example = "Love")
    @NotBlank(message = "The title of the book is required and cannot be empty.")
    private String title;

    @Schema(description = "Year of the book's publication. Must be in the past or present.", example = "2020")
    @NotNull(message = "The publication year is required and cannot be null.")
    @PastOrPresent(message = "The publication year must be in the past or the present.")
    private Year publicationYear;

    @Schema(description = "List of authors who wrote the book. At least one author is required.")
    @NotEmpty(message = "At least one author is required for the book.")
    @Valid
    private List<AuthorDto> authors;

    @Schema(description = "Publisher of the book")
    @NotNull(message = "The publisher is required and cannot be null.")
    @Valid
    private PublisherDto publisher;

    @Schema(description = "Cover image of the book. Can be null if no cover is provided.")
    @Valid
    @Nullable
    private Image image;
}
