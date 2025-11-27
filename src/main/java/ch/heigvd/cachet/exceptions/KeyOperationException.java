package ch.heigvd.cachet.exceptions;

/**
 * Exception thrown when a key operation fails.
 */
public class KeyOperationException extends CachetException {
    public KeyOperationException(String message) {
        super(message);
    }

    public KeyOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
