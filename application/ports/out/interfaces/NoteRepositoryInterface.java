package application.ports.out.interfaces;

import java.util.List;

import application.ports.in.dto.CreateNoteDto;
import domain.Note;

public interface NoteRepositoryInterface {// интерфейс адаптера

    // beta
    public void add(CreateNoteDto note);
    public void update(Note note);
    public void delete(int noteId);
    public Note findById(int noteId);
    public List<Note> findAll();

}