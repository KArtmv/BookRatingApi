package ua.foxminded.bookrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data transfer object for creating or updating an Author")
public record AuthorDto(
        @Schema(description = "Name of the author", example = "William")
        @NotBlank(message = "Author name is required and cannot be empty.")
        String name
) {
}
