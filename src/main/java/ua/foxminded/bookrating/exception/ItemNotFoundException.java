package ua.foxminded.bookrating.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long itemId) {
        super("Item not found with id: " + itemId);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
