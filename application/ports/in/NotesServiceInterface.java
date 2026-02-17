package application.ports.in;

import java.util.List;

import application.ports.in.dto.CreateNoteDto;
import application.ports.in.dto.NoteDto;

public interface NotesServiceInterface {// интерфейс сценариев

    //beta
    public NoteDto createNote(CreateNoteDto dto);
    public List<NoteDto> listNotes();
}
