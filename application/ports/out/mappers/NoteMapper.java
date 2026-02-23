package application.ports.out.mappers;

import java.util.List;

import application.ports.in.dto.CreateNoteDto;
import application.ports.in.dto.NoteDto;
import domain.Note;
import domain.interfaces.NoteInterface;
import domain.interfaces.TagsInterface;

public class NoteMapper {
    
    public static NoteDto toNoteDto(NoteInterface note) {
        // Note в NoteDto
        return new NoteDto(note.getUidNote(), note.getTitleName(), note.getTextMain(), note.getTagsList().stream().map(TagMapper::toStringTag).toList());
    }

    public static NoteInterface toNote(CreateNoteDto dto, List<TagsInterface> tags){
        // CreateNoteDto в Note
        return new Note(dto.title(), dto.content(), tags);
    }
    public static NoteInterface toNote(NoteDto dto, List<TagsInterface> tags){
        // NoteDto в Note
        return new Note(dto.title(), dto.content(), dto.noteId(), tags);
    }

}
