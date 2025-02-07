package ua.foxminded.bookrating.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record PublisherDto(
        @NotBlank(message = "Publisher name is required and cannot be empty.")
        String name
) {
}
