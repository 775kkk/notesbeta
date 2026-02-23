package application.exceptions;

public class TagNotFoundException extends NotFoundException {
    public TagNotFoundException(String message) {
        super(message);
    }
    public TagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public TagNotFoundException(Throwable cause) {
        super(cause);
    }
    public TagNotFoundException() {
        super();
    }
    
}
