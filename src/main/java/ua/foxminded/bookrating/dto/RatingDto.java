package ua.foxminded.bookrating.dto;

public record RatingDto(Long bookId, Long userId, Integer bookRating) {
}
