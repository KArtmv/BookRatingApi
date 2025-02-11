package ua.foxminded.bookrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data transfer object for creating or updating a Publisher")
public record PublisherDto(
        @Schema(description = "Name of the publisher", example = "HarperCollins")
        @NotBlank(message = "Publisher name is required and cannot be empty.")
        String name
) {
}
