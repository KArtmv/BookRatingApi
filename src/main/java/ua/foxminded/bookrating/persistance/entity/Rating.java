package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rating")
@SequenceGenerator(name = "default_gen", sequenceName = "rating_id_seq", allocationSize = 10000)
public class Rating extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer bookRating;

    public Rating(Book book, User user, Integer bookRating) {
        this.book = book;
        this.user = user;
        this.bookRating = bookRating;
    }

    public Rating(Long id, Book book, User user, Integer bookRating) {
        super(id);
        this.book = book;
        this.user = user;
        this.bookRating = bookRating;
    }

    public void setBook(Book book) {
        this.book = book;
        book.getRatings().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.getRatings().add(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Rating rating = (Rating) o;
        return getId() != null && Objects.equals(getId(), rating.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}