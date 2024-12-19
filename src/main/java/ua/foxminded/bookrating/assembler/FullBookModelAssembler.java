package ua.foxminded.bookrating.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.controller.BookController;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.BookModel;
import ua.foxminded.bookrating.model.PublisherModel;
import ua.foxminded.bookrating.persistance.entity.Book;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class FullBookModelAssembler implements RepresentationModelAssembler<Book, BookModel> {

    private final AuthorModelAssembler authorModelAssembler;
    private final PublisherModelAssembler publisherModelAssembler;

    @Override
    public BookModel toModel(Book entity) {
        BookModel bookModel = new BookModel();
        bookModel.setId(entity.getId());
        bookModel.setIsbn(entity.getIsbn());
        bookModel.setTitle(entity.getTitle());
        bookModel.setPublicationYear(entity.getPublicationYear());
        bookModel.setAverageRating(entity.getAverageRating());
        bookModel.setImage(entity.getImage());
        bookModel.setAuthors(getAuthorsModel(entity));
        bookModel.setPublisher(getPublisherModel(entity));
        bookModel.add(getRatingsLink(entity));
        return bookModel;
    }

    private List<AuthorModel> getAuthorsModel(Book entity) {
        return entity.getAuthors().stream().map(authorModelAssembler::toModel).toList();
    }

    private PublisherModel getPublisherModel(Book entity) {
        return publisherModelAssembler.toModel(entity.getPublisher());
    }

    private static Link getRatingsLink(Book entity) {
        return linkTo(methodOn(BookController.class).getBookRatings(entity.getId(), Pageable.unpaged())).withRel("bookRatings");
    }
}
