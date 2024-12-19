package ua.foxminded.bookrating.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.projection.BookRatingProjection;

@Component
@RequiredArgsConstructor
public class BookModelAssembler implements RepresentationModelAssembler<BookRatingProjection, SimpleBookModel> {

    private final SimpleBookModelAssembler simpleBookModelAssembler;

    @Override
    public SimpleBookModel toModel(BookRatingProjection entity) {
        return simpleBookModelAssembler.toModel(entity.getBook());
    }
}
