package application.ports.out;

import java.util.HashMap;
import java.util.List;

import application.ports.out.interfaces.NoteRepositoryInterface;
import domain.interfaces.NoteInterface;

public class NoteRepository implements NoteRepositoryInterface {
    private final HashMap<Integer, NoteInterface> notes = new HashMap<>();

     // beta
    @Override
    public void add(NoteInterface createNote) {
        if (!notes.containsKey(createNote.getUidNote())) {
            notes.put(createNote.getUidNote(), createNote);
        }
    }

    @Override
    public void update(NoteInterface note) {
        if (notes.containsKey(note.getUidNote())) {
            notes.put(note.getUidNote(), note);
        }
    }

    @Override
    public void delete(int noteId) {
        notes.remove(noteId);
    }

    @Override
    public NoteInterface findById(int noteId) {
        return notes.get(noteId);
    }


    @Override
    public List<NoteInterface> findAll() {
        return notes.values().stream().toList();
    }
    
}
