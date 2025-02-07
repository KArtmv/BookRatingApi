package ua.foxminded.bookrating.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import ua.foxminded.bookrating.persistance.entity.Image;

import java.time.Year;
import java.util.List;

@Getter
@Setter
public class SimpleBookModel extends RepresentationModel<SimpleBookModel> {
    private Long id;
    private String title;
    private List<String> author;
    private String publisher;
    private Year publicationYear;
    private Double averageRating;
    private Image image;
}
