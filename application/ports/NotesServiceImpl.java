package application.ports;

import application.ports.interfaces.NotesServiceInterface;
import application.ports.out.interfaces.NoteRepositoryInterface;
import application.ports.out.interfaces.TagRepositoryInterface;

public class NotesServiceImpl implements NotesServiceInterface {
    private final NoteRepositoryInterface noteRepository;
    private final TagRepositoryInterface tagRepository;
    
    public NotesServiceImpl(NoteRepositoryInterface noteRepository, TagRepositoryInterface tagRepository) {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
    }


}
