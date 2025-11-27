package ch.heigvd.cachet.exceptions;

/**
 * Base exception for all Cachet application errors.
 */
public class CachetException extends RuntimeException {
    public CachetException(String message) {
        super(message);
    }

    public CachetException(String message, Throwable cause) {
        super(message, cause);
    }
}
