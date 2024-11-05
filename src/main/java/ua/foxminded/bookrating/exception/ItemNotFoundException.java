package ua.foxminded.bookrating.exception;

public class ItemNotFoundException extends RuntimeException {

    private Long itemId;

    public ItemNotFoundException(Long itemId) {
        super("Item not found with id " + itemId);
    }
}
