package application.ports.out;

import java.util.HashMap;
import java.util.List;

import application.ports.out.interfaces.NoteRepositoryInterface;
import domain.interfaces.NoteInterface;

public class NoteRepository implements NoteRepositoryInterface {
    private final HashMap<Integer, NoteInterface> notes = new HashMap<>();

     // beta
    @Override
    public NoteInterface save(NoteInterface createNote) throws {//TODO
        if (!notes.containsKey(createNote.getUidNote())) {
            return notes.put(createNote.getUidNote(), createNote);// put ошибку?
        }
        throw new //TODO тут кастом ошибка что нота с таким id уже существует
    }

    @Override
    public void update(NoteInterface note) throws {//TODO
        if (notes.containsKey(note.getUidNote())) {
            notes.put(note.getUidNote(), note);// put ошибку?
            return;
        }
        throw new //TODO тут кастом ошибка неудачи метода
    }

    @Override
    public void delete(int noteId) throws {//TODO
        if (notes.containsKey(noteId)) {
            notes.remove(noteId);// remove ошибку?
        } else {
            throw new //TODO кастом ошибка удаления несуществующей ноты
        }
    }

    @Override
    public NoteInterface findById(int noteId) throws {//TODO
        if (notes.containsKey(noteId)) {
            return notes.get(noteId);// get ошибку?
        }
        throw new //TODO кастом ошибка поиска несуществующей ноты
    }


    @Override
    public List<NoteInterface> findAll() {// ошибку?
        if (notes.isEmpty()) {
            return List.of();
        }
        return notes.values().stream().toList();
    }
    
}
