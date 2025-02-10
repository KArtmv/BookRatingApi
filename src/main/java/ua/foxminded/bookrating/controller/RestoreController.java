package ua.foxminded.bookrating.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.service.RestoreService;

public class RestoreController<T extends BaseEntity, D, M extends RepresentationModel<?>> extends CrudController<T, D, M> {

    private final RestoreService<T, D> restoreService;

    protected RestoreController(RestoreService<T, D> restoreService, RepresentationModelAssembler<T, M> modelAssembler) {
        super(restoreService, modelAssembler);
        this.restoreService = restoreService;
    }

    @PutMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        restoreService.restoreById(id);
        return ResponseEntity.noContent().build();
    }
}
