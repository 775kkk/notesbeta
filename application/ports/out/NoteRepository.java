package application.ports.out;

import java.util.HashMap;
import java.util.List;

import application.ports.in.dto.CreateNoteDto;
import application.ports.out.interfaces.NoteRepositoryInterface;
import domain.Note;

public class NoteRepository implements NoteRepositoryInterface {
    private final HashMap<Integer, Note> notes = new HashMap<>();

     // beta
     @Override
     public void add(CreateNoteDto createNoteDto) {
        if (createNoteDto != null) {
            Note note = createNoteDto.toNote();
            notes.put(note.getId(), note);
        }
    }

     @Override
     public void update(Note note) {
         // TODO Auto-generated method stub
         
     }

     @Override
     public void delete(int noteId) {
         // TODO Auto-generated method stub
         
     }

     @Override
     public Note findById(int noteId) {
         // TODO Auto-generated method stub
         return null;
     }

     @Override
     public List<Note> findAll() {
         // TODO Auto-generated method stub
         return null;
     }
    
}
