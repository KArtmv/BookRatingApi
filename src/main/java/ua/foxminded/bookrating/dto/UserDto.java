package ua.foxminded.bookrating.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(@NotBlank(message = "Location can't be blank") String location, Integer age) {
}
