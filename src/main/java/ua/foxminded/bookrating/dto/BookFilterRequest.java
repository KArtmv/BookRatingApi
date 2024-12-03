package ua.foxminded.bookrating.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BookFilterRequest(
        @NotEmpty(message = "Required parameter 'authors' is not present.") List<Long> authorsId,
        @NotEmpty(message = "Required parameter 'publisher' is not present.") List<Long> publishersId) {
}
