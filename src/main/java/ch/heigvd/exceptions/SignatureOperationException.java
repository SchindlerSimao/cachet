package ch.heigvd.exceptions;

/**
 * Exception thrown when a signature operation fails.
 */
public class SignatureOperationException extends CachetException {
    public SignatureOperationException(String message) {
        super(message);
    }

    public SignatureOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
