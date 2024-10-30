package ua.foxminded.bookrating.assembler;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.controller.BookController;
import ua.foxminded.bookrating.model.SimpleBookModel;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SimpleBookModelAssembler implements RepresentationModelAssembler<Book, SimpleBookModel> {

    @Override
    public SimpleBookModel toModel(Book entity) {
        SimpleBookModel simpleBookModel = new SimpleBookModel();
        simpleBookModel.setId(entity.getId());
        simpleBookModel.setTitle(entity.getTitle());
        simpleBookModel.setAuthor(getAuthorsName(entity));
        simpleBookModel.setPublisher(getPublisherName(entity));
        simpleBookModel.setPublicationYear(entity.getPublicationYear());
        simpleBookModel.setAverageRating(entity.getAverageRating());
        simpleBookModel.setImage(entity.getImage());
        simpleBookModel.add(getSelfLink(entity));
        return simpleBookModel;
    }

    private List<String> getAuthorsName(Book book) {
        return book.getAuthors().stream().map(Author::getName).toList();
    }

    private String getPublisherName(Book book) {
        return book.getPublisher().getName();
    }

    private Link getSelfLink(Book book) {
        return linkTo(methodOn(BookController.class).getBook(book.getId())).withSelfRel();
    }
}

