package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String location;

    private Integer age;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Rating> ratings = new LinkedHashSet<>();

    public User(Long id) {
        super(id);
    }

    public User(Long id, String location, Integer age) {
        super(id);
        this.location = location;
        this.age = age;
    }
}