package application.exceptions;

public class ValidationException extends AppException {
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidationException(Throwable cause) {
        super(cause);
    }
    public ValidationException() {
        super();
    }
    
}
