package application.ports.out;

import java.util.List;

import domain.Note;

public interface NoteRepositoryInterface {// интерфейс адаптера

    // beta
    public void add(Note note);
    public void update(Note note);
    public void delete(int noteId);
    public Note findById(int noteId);
    public List<Note> findAll();

}