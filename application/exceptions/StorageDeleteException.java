package application.exceptions;

public class StorageDeleteException extends StorageException {
    public StorageDeleteException(String message) {
        super(message);
    }
    public StorageDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
    public StorageDeleteException(Throwable cause) {
        super(cause);
    }
    public StorageDeleteException() {
        super();
    }
    
}
