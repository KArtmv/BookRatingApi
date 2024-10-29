package ua.foxminded.bookrating.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class AuthorModel extends RepresentationModel<AuthorModel> {
    private Long id;
    private String name;
}
