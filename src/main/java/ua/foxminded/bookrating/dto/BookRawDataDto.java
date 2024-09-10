package ua.foxminded.bookrating.dto;

public record BookRawDataDto(String isbn,
                             String title,
                             String author,
                             String publicationYear,
                             String publisher,
                             String imageUrlS,
                             String imageUrlM,
                             String imageUrlL) {
}
