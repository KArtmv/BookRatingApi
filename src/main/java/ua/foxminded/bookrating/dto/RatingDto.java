package ua.foxminded.bookrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data transfer object for creating or updating a book rating")
public record RatingDto(
        @Schema(description = "ID of the book being rated", hidden = true)
        Long bookId,
        @Schema(description = "ID of the user who submitted the rating", hidden = true)
        Long userId,
        @Schema(description = "Rating score given to the book (1 to 10)", example = "4")
        @NotNull(message = "A rating value is required.")
        @Min(value = 1, message = "The minimum allowed rating is 1.")
        @Max(value = 10, message = "The maximum allowed rating is 10.")
        Integer bookRating) {
}
