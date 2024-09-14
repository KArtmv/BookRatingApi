package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book")
@SequenceGenerator(name = "default_gen", sequenceName = "book_id_seq", allocationSize = 1)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    @NotNull(message = "A publisher is required.")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "authors_id"))
    private Set<Author> author = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private Set<Rating> ratings = new LinkedHashSet<>();

    public Book(Long id) {
        super(id);
    }

    public void addAuthor(Author author) {
        this.author.add(author);
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }
}
