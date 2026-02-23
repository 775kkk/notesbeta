package application.exceptions;

public class NoteNotFoundException extends NotFoundException {
    public NoteNotFoundException(String message) {
        super(message);
    }
    public NoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoteNotFoundException(Throwable cause) {
        super(cause);
    }
    public NoteNotFoundException() {
        super();
    }
    
}
