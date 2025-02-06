package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@SequenceGenerator(name = "default_gen", sequenceName = "user_id_seq", allocationSize = 1)
public class User extends BaseEntity {

    @NotNull(message = "Location is required")
    @NotBlank(message = "Location can't be blank")
    private String location;

    private Integer age;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Rating> ratings = new LinkedHashSet<>();

    public User(String location, Integer age) {
        this.location = location;
        this.age = age;
    }

    public User(Long id, String location, Integer age) {
        super(id);
        this.location = location;
        this.age = age;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}