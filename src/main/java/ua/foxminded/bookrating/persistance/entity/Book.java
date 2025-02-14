package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book")
@SequenceGenerator(name = "default_gen", sequenceName = "book_id_seq", allocationSize = 1)
public class Book extends BaseEntity {

    @Column(name = "isbn", nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publication_year", nullable = false)
    private Year publicationYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToMany
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_isbn", referencedColumnName = "isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new LinkedHashSet<>();

    @Embedded
    private Image image;

    @OneToMany(mappedBy = "book")
    private Set<Rating> ratings = new LinkedHashSet<>();

    public Book(String isbn, String title, Year publicationYear, Publisher publisher, Set<Author> authors, Image image) {
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.authors = authors;
        this.image = image;
    }

    public Book(Long id, String isbn, String title, Year publicationYear, Publisher publisher, Set<Author> authors, Image image) {
        super(id);
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.authors = authors;
        this.image = image;
    }

    public Double getAverageRating() {
        return ratings.stream().mapToDouble(Rating::getBookRating)
                .average().orElse(0.0);
    }

    public void setAuthors(Set<Author> authors) {
        if (!authors.isEmpty()) {
            authors.forEach(a -> a.getBooks().remove(this));
        }
        this.authors = authors;
        if (!authors.isEmpty()) {
            authors.forEach(author -> author.getBooks().add(this));
        }
    }

    public void setPublisher(Publisher publisher) {
        if (this.publisher != null) {
            this.publisher.getBooks().remove(this);
        }
        this.publisher = publisher;
        if (publisher != null) {
            publisher.getBooks().add(this);
        }
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
