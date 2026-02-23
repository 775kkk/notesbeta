package application.exceptions;

public class DuplicateTagException extends ConflictException {
    public DuplicateTagException(String message) {
        super(message);
    }
    public DuplicateTagException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateTagException(Throwable cause) {
        super(cause);
    }
    public DuplicateTagException() {
        super();
    }
    
}
