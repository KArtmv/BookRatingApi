package ua.foxminded.bookrating.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class PublisherModel extends RepresentationModel<PublisherModel> {
    private Long id;
    private String name;
}
