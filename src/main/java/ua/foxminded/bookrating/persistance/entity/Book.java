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
    @Column(name = "isbn")
    private String isbn;

    @NotBlank(message = "The title of the book is required and cannot be empty")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "The year of publication is required")
    @Pattern(regexp = "^[0-9]+", message = "The year can contains just digits")
    @Size(max = 4, message = "The length of year is 4 characters")
    @Column(name = "publication_year")
    private String publicationYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @NotNull(message = "A publisher is required.")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_isbn", referencedColumnName = "isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new LinkedHashSet<>();

    @Embedded
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (getId() != null && book.getId() != null) {
            return getId().equals(book.getId());
        }

        return isbn != null && isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : (isbn != null ? isbn.hashCode() : 0);
    }
}
