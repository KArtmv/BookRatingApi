package ua.foxminded.bookrating.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
public class RatingModel extends RepresentationModel<RatingModel> {
    private Long id;
    private UserModel user;
    private SimpleBookModel book;
    private Integer rating;
}
