package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book")
@SequenceGenerator(name = "default_gen", sequenceName = "book_id_seq", allocationSize = 1)
@SQLInsert(sql = """
        INSERT INTO book (image_id, isbn, publication_year, publisher_id, title, id)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT (isbn) DO NOTHING""")
public class Book extends BaseEntity {

    @NotBlank(message = "The ISBN of book is required")
    @Size(min = 10, max = 13, message = "The ISBN must be between 10 and 13 characters long")
    private String isbn;

    @NotBlank(message = "The title of the book is required and cannot be empty")
    private String title;

    @NotBlank(message = "The year of publication is required")
    @Pattern(regexp = "^[0-9]+", message = "The year can contains just digits")
    @Size(max = 4, message = "The length of year is 4 characters")
    private String publicationYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @NotNull(message = "A publisher is required.")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_isbn", referencedColumnName = "isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @SQLInsert(sql = """
            INSERT INTO book_authors (book_isbn, author_id)
                        VALUES (?, ?)
                        ON CONFLICT (book_isbn, author_id) DO NOTHING""")
    private Set<Author> authors = new LinkedHashSet<>();


    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Rating> ratings = new LinkedHashSet<>();

    public Book(String isbn, String title, String publicationYear, Publisher publisher, Set<Author> authors, Image image) {
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.authors = authors;
        this.image = image;
    }

    public Book(Long id, String isbn, String title, String publicationYear, Publisher publisher, Set<Author> authors, Image image) {
        super(id);
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.authors = authors;
        this.image = image;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public Double getAverageRating() {
        return ratings.stream().mapToDouble(Rating::getBookRating)
                .average().orElse(0.0);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
