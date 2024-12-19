package ua.foxminded.bookrating.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.controller.UserController;
import ua.foxminded.bookrating.model.UserModel;
import ua.foxminded.bookrating.persistance.entity.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, UserModel> {

    @Override
    public UserModel toModel(User entity) {
        UserModel userModel = new UserModel();
        userModel.setId(entity.getId());
        userModel.setLocation(entity.getLocation());
        userModel.setAge(entity.getAge());
        userModel.add(getRatedBooksLink(entity));
        userModel.add(getSelfLink(entity));
        return userModel;
    }

    private Link getRatedBooksLink(User user) {
        return linkTo(methodOn(UserController.class).getRatedBooksByUser(user.getId(), Pageable.unpaged())).withRel("ratedBooks");
    }

    private Link getSelfLink(User user) {
        return linkTo(methodOn(UserController.class).get(user.getId())).withSelfRel();
    }
}
