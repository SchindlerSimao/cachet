package ch.heigvd.exceptions;

/**
 * Exception thrown when a file operation fails.
 */
public class FileOperationException extends CachetException {
    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
