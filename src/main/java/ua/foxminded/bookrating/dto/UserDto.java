package ua.foxminded.bookrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Data transfer object for creating or updating a user")
public record UserDto(
        @Schema(description = "User's place of residence", example = "New York")
        @NotBlank(message = "Location is required and cannot be blank")
        String location,
        @Schema(description = "User's age", example = "25")
        @PositiveOrZero(message = "Age must be a positive number or zero")
        Integer age) {
}
