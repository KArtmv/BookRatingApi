package ua.foxminded.bookrating.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import ua.foxminded.bookrating.persistance.entity.Image;

import java.time.Year;
import java.util.List;

@Getter
@Setter
public class BookModel extends RepresentationModel<BookModel> {
    private Long id;
    private String isbn;
    private String title;
    private Year publicationYear;
    private Double averageRating;
    private List<AuthorModel> authors;
    private PublisherModel publisher;
    private Image image;
}
