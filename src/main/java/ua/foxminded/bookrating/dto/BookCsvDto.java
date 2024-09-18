package ua.foxminded.bookrating.dto;

public record BookCsvDto(String isbn,
                         String title,
                         String author,
                         String publicationYear,
                         String publisher,
                         String imageUrlS,
                         String imageUrlM,
                         String imageUrlL) {
}
