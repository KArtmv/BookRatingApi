package ua.foxminded.bookrating.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.controller.AuthorController;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.persistance.entity.Author;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorModelAssembler implements RepresentationModelAssembler<Author, AuthorModel> {

    @Override
    public AuthorModel toModel(Author entity) {
        AuthorModel authorModel = new AuthorModel();
        authorModel.setId(entity.getId());
        authorModel.setName(entity.getName());
        authorModel.add(getAuthorBooksLink(entity));
        authorModel.add(getSelfLink(entity));

        return authorModel;
    }

    private Link getSelfLink(Author entity) {
        return linkTo(methodOn(AuthorController.class).get(entity.getId())).withSelfRel();
    }

    private Link getAuthorBooksLink(Author entity) {
        return linkTo(methodOn(AuthorController.class)
                .getAuthorBooks(entity.getId(), Pageable.unpaged())).withRel("authorBooks");
    }
}
