package ua.foxminded.bookrating.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.controller.BookController;
import ua.foxminded.bookrating.controller.RatingController;
import ua.foxminded.bookrating.dto.RatingDto;
import ua.foxminded.bookrating.model.AuthorModel;
import ua.foxminded.bookrating.model.BookModel;
import ua.foxminded.bookrating.model.PublisherModel;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Publisher;

import java.util.List;
import java.util.Set;

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
        bookModel.setAuthors(getAuthorsModel(entity.getAuthors()));
        bookModel.setPublisher(getPublisherModel(entity.getPublisher()));
        bookModel.add(getRatingsLink(entity.getId()));
        bookModel.add(getLinkToRateBook(entity.getId()));
        return bookModel;
    }

    private List<AuthorModel> getAuthorsModel(Set<Author> authors) {
        return authors.stream().map(authorModelAssembler::toModel).toList();
    }

    private PublisherModel getPublisherModel(Publisher publisher) {
        return publisherModelAssembler.toModel(publisher);
    }

    private Link getRatingsLink(Long bookId) {
        return linkTo(methodOn(BookController.class).getBookRatings(bookId, Pageable.unpaged())).withRel("bookRatings");
    }

    private Link getLinkToRateBook(Long bookId) {
        return linkTo(methodOn(RatingController.class).add(new RatingDto(bookId, null, null)))
                .withRel("rateBook")
                .withType("POST");
    }
}
