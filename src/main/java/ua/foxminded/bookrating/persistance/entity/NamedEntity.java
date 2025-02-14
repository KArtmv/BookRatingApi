package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    protected NamedEntity(String name) {
        this.name = name;
    }

    protected NamedEntity(Long id, String name) {
        super(id);
        this.name = name;
    }
}
