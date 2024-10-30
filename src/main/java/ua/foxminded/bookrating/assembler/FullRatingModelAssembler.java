package ua.foxminded.bookrating.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.model.RatingModel;
import ua.foxminded.bookrating.persistance.entity.Rating;

@Component
@RequiredArgsConstructor
public class FullRatingModelAssembler implements RepresentationModelAssembler<Rating, RatingModel> {

    private final UserModelAssembler userModelAssembler;
    private final SimpleBookModelAssembler bookModelAssembler;

    @Override
    public RatingModel toModel(Rating entity) {
        return RatingModel.builder()
                .id(entity.getId())
                .book(bookModelAssembler.toModel(entity.getBook()))
                .user(userModelAssembler.toModel(entity.getUser()))
                .build();
    }
}
