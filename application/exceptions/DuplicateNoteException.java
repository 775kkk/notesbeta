package application.exceptions;

public class DuplicateNoteException extends ConflictException {
    public DuplicateNoteException(String message) {
        super(message);
    }
    public DuplicateNoteException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateNoteException(Throwable cause) {
        super(cause);
    }
    public DuplicateNoteException() {
        super();
    }
    
}
