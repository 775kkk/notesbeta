package application.exceptions;

public class StorageException extends AppException {
    public StorageException(String message) {
        super(message);
    }
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
    public StorageException(Throwable cause) {
        super(cause);
    }
    public StorageException() {
        super();
    }
    
}
