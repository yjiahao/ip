package echo.exception;

/**
 * Exceptions related to the Storage class.
 */
public class StorageException extends Exception {
    /**
     * Constructor for StorageException
     * @param message Message for the exception
     */
    public StorageException(String message) {
        super(message);
    }
}
