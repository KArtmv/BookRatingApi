package ua.foxminded.bookrating.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.controller.RatingController;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.persistance.entity.Rating;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class RatingModelAssembler implements RepresentationModelAssembler<Rating, RatingModel> {

    private final UserModelAssembler userModelAssembler;

    @Override
    public RatingModel toModel(Rating entity) {
        return RatingModel.builder()
                .user(userModelAssembler.toModel(entity.getUser()))
                .rating(entity.getBookRating())
                .build()
                .add(getSelfLink(entity));
    }

    private Link getSelfLink(Rating rating) {
        return linkTo(methodOn(RatingController.class).get(rating.getId())).withSelfRel();
    }
}
