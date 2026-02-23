package application.ports.out.mappers;

import java.util.List;

import application.ports.in.dto.CreateNoteDto;
import application.ports.in.dto.NoteDto;
import domain.Note;
import domain.interfaces.NoteInterface;

public class NoteMapper {
    
    public static NoteDto toNoteDto(NoteInterface note) {
        // Note в NoteDto
        return new NoteDto(note.getUidNote(), note.getTitleName(), note.getTextMain(), note.getTagsList().stream().map(TagMapper::toStringTag).toList());
    }

    public static NoteInterface toNote(NoteDto noteDto) {
        // NoteDto в Note
        NoteInterface note = new Note(noteDto.title(), noteDto.content(), noteDto.noteId(), List.of());
        // списк тегов из строк в TagsInterface
        if (noteDto.tags()!=null) {
            // note.setTagsList(noteDto.tags().stream().map(x -> TagMapper.StringToTag(x)).toList());
            // Tags через TagsServiceInterface или нет?
            // StringToTag(x) должен быть в TagInterface?
        }
        return note;
    }
    public static NoteInterface toNote(CreateNoteDto createNoteDto) {
        //  CreateNoteDto в Note
        NoteInterface note = new Note(createNoteDto.title(), createNoteDto.content(), List.of());
        // списк тегов из строк в TagsInterface
        if (createNoteDto.tags()!=null) {
            // note.setTagsList(createNoteDto.tags().stream().map(x -> TagMapper.StringToTag(x)).toList());
            // Tags через TagsServiceInterface или нет?
        }
        return note;
    }

}
