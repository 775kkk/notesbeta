package application.ports.out;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import application.exceptions.DuplicateNoteException;
import application.exceptions.NoteNotFoundException;
import application.ports.out.interfaces.NoteRepositoryInterface;
import domain.interfaces.NoteInterface;

public class NoteRepository implements NoteRepositoryInterface {
    private final HashMap<Integer, NoteInterface> notes = new LinkedHashMap<>();

     // beta
    @Override
    public NoteInterface save(NoteInterface createNote) throws DuplicateNoteException {
        if (!notes.containsKey(createNote.getUidNote())) {
            notes.put(createNote.getUidNote(), createNote);
            return createNote;
        }
        throw new DuplicateNoteException("Note with ID " + createNote.getUidNote() + " already exists");
    }

    @Override
    public void update(NoteInterface note) throws NoteNotFoundException{
        if (notes.containsKey(note.getUidNote())) {
            notes.put(note.getUidNote(), note);// put ошибку?
            return;
        }//TODO СДЕЛАТЬ УМНЫЙ АПДЕЙТ
        throw new NoteNotFoundException("Failed to update note with ID " + note.getUidNote());

    }

    @Override
    public void delete(int noteId) throws NoteNotFoundException {
        if (notes.containsKey(noteId)) {
            notes.remove(noteId);// remove ошибку?
        } else {
            throw new NoteNotFoundException("Failed to delete note with ID " + noteId);
        }
    }

    @Override
    public NoteInterface findById(int noteId) throws NoteNotFoundException {
        if (notes.containsKey(noteId)) {
            return notes.get(noteId);// get ошибку?
        }
        throw new NoteNotFoundException("Failed to find note with ID " + noteId);
    }


    @Override
    public List<NoteInterface> findAll() {// ошибку?
        if (notes.isEmpty()) {
            return List.of();
        }
        return notes.values().stream().toList();
    }
    
}
