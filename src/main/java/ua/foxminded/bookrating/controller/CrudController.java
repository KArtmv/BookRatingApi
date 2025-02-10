package ua.foxminded.bookrating.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;
import ua.foxminded.bookrating.service.CrudService;

@Validated
public class CrudController<T extends BaseEntity, D, M extends RepresentationModel<?>> {

    private final CrudService<T, D> crudService;
    private final RepresentationModelAssembler<T, M> modelAssembler;

    public CrudController(CrudService<T, D> crudService, RepresentationModelAssembler<T, M> modelAssembler) {
        this.crudService = crudService;
        this.modelAssembler = modelAssembler;
    }

    @GetMapping("/{id}")
    public M get(@PathVariable("id") Long id) {
        return modelAssembler.toModel(crudService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public M add(@RequestBody @Valid D dto) {
        return modelAssembler.toModel(crudService.create(dto));
    }

    @PutMapping("/{id}")
    public M update(@PathVariable Long id, @Valid @RequestBody D entity) {
        return modelAssembler.toModel(crudService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        crudService.delete(id);
    }
}
