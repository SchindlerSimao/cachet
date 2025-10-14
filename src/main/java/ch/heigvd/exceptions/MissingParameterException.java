package ch.heigvd.exceptions;

/**
 * Exception thrown when a parameter is missing.
 */
public class MissingParameterException extends CachetException {
    public MissingParameterException(String message) {
        super(message);
    }

    public MissingParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
