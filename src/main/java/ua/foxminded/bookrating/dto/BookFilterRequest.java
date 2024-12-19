package ua.foxminded.bookrating.dto;

import ua.foxminded.bookrating.annotation.Filter;

import java.util.List;

@Filter(author = "authorsId", publisher = "publishersId")
public record BookFilterRequest(List<Long> authorsId, List<Long> publishersId) {
}
