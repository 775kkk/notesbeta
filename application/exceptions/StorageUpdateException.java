package application.exceptions;

public class StorageUpdateException extends StorageException {
    public StorageUpdateException(String message) {
        super(message);
    }
    public StorageUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
    public StorageUpdateException(Throwable cause) {
        super(cause);
    }
    public StorageUpdateException() {
        super();
    }
    
}
