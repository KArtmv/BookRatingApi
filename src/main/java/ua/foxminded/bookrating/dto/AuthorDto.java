package ua.foxminded.bookrating.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record AuthorDto(
        @NotBlank(message = "Author name is required and cannot be empty.")
        String name
) {
}
