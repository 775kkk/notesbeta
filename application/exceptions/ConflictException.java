package application.exceptions;

public class ConflictException extends AppException {
    public ConflictException(String message) {
        super(message);
    }
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConflictException(Throwable cause) {
        super(cause);
    }
    public ConflictException() {
        super();
    }
    
}
