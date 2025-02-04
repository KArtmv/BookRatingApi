package ua.foxminded.bookrating.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.bookrating.persistance.entity.Author;
import ua.foxminded.bookrating.persistance.entity.Book;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.persistance.entity.Rating;

import java.util.List;

public class BookSpecification {

    public static Specification<Book> hasTitle(String title) {
        return (root, query, builder) -> {
            if (title == null || title.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(
                    builder.lower(root.get("title")), "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Book> hasAverageRating(Integer averageRating) {
        return (root, query, criteriaBuilder) -> {
            if (averageRating == null) {
                return criteriaBuilder.conjunction();
            }

            Subquery<Double> subquery = query.subquery(Double.class);
            Root<Rating> ratingRoot = subquery.from(Rating.class);

            subquery.select(criteriaBuilder.avg(ratingRoot.get("bookRating")))
                    .where(criteriaBuilder.equal(ratingRoot.get("book"), root));

            return criteriaBuilder.greaterThanOrEqualTo(subquery, averageRating.doubleValue());
        };
    }

    public static Specification<Book> hasPublicationYear(Integer year) {
        return ((root, query, criteriaBuilder) -> {
            if (year == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("publicationYear"), year);
        });
    }

    public static Specification<Book> hasAuthors(List<Author> authors) {
        return (root, query, criteriaBuilder) -> {
            if (authors == null || authors.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.in(root.join("authors")).value(authors);
        };
    }

    public static Specification<Book> hasPublishers(List<Publisher> publishers) {
        return (root, query, criteriaBuilder) -> {
            if (publishers == null || publishers.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.in(root.join("publisher")).value(publishers);
        };
    }
}
