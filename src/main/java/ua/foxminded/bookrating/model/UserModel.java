package ua.foxminded.bookrating.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class UserModel extends RepresentationModel<UserModel> {
    private Long id;
    private String location;
    private Integer age;
}
