package ua.foxminded.bookrating.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UserDto(@NotBlank(message = "Location can't be blank") String location,
                      @PositiveOrZero(message = "Age can be only a positive value or zero") Integer age) {
}
