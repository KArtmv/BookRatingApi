package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "publisher")
@SequenceGenerator(name = "default_gen", sequenceName = "publisher_is_seq", allocationSize = 1)
public class Publisher extends BaseEntity {

    @NotBlank(message = "The publisher name is required and cannot be empty.")
    private String name;

    @OneToMany(mappedBy = "publisher", orphanRemoval = true)
    private Set<Book> books = new LinkedHashSet<>();

    public Publisher(String name) {
        this.name = name;
    }

    public Publisher(Long id) {
        super(id);
    }
}