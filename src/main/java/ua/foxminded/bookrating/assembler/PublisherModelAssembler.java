package ua.foxminded.bookrating.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.controller.PublisherController;
import ua.foxminded.bookrating.model.PublisherModel;
import ua.foxminded.bookrating.persistance.entity.Publisher;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PublisherModelAssembler implements RepresentationModelAssembler<Publisher, PublisherModel> {

    @Override
    public PublisherModel toModel(Publisher entity) {
        PublisherModel publisherModel = new PublisherModel();
        publisherModel.setId(entity.getId());
        publisherModel.setName(entity.getName());
        publisherModel.add(getSelfLink(entity));
        publisherModel.add(getPublisherBooksLink(entity));
        return publisherModel;
    }

    private Link getPublisherBooksLink(Publisher entity) {
        return linkTo(methodOn(PublisherController.class).getPublisherBooks(entity.getId(), Pageable.unpaged(), 0)).withRel("publisherBooks");
    }

    private Link getSelfLink(Publisher entity) {
        return linkTo(methodOn(PublisherController.class).get(entity.getId())).withSelfRel();
    }
}
