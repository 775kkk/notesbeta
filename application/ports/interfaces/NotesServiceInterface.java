package application.ports.interfaces;

import java.util.List;

import application.ports.in.dto.CreateNoteDto;
import application.ports.in.dto.NoteDto;

public interface NotesServiceInterface {// интерфейс сценариев

    //beta
    public NoteDto createNote(CreateNoteDto dto);
    public void deleteNote(int noteId);
    public void updateNote(NoteDto noteDto);
    public NoteDto getNoteById(int noteId);
    public List<NoteDto> listNotes();
}
