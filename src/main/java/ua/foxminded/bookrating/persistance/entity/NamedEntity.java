package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {

    @NotBlank(message = "The name is required and cannot be empty.")
    private String name;

    protected NamedEntity(String name) {
        this.name = name;
    }

    protected NamedEntity(Long id, String name) {
        super(id);
        this.name = name;
    }
}
