package application.ports;

import java.util.List;

import application.ports.in.dto.CreateNoteDto;
import application.ports.in.dto.NoteDto;
import application.ports.interfaces.NotesServiceInterface;
import application.ports.out.interfaces.NoteRepositoryInterface;
import application.ports.out.interfaces.TagRepositoryInterface;
import application.ports.out.mappers.NoteMapper;
import domain.interfaces.TagsInterface;

public class NotesServiceImpl implements NotesServiceInterface {
    private final NoteRepositoryInterface noteRepository;
    
    public NotesServiceImpl(NoteRepositoryInterface noteRepository) {
        this.noteRepository = noteRepository;
    }

//========================NOTE========================
    @Override
    public NoteDto createNote(CreateNoteDto dto) throws{// TODO кастом ошибка если нота с таким id есть
        return NoteMapper.toNoteDto(noteRepository.save(NoteMapper.toNote(dto)));
    }

    @Override
    public void deleteNote(int noteId) throws{// TODO кастом ошибка удаления
        //TODO 
        noteRepository.delete(noteId);
    }

    @Override
    public void updateNote(NoteDto noteDto) throws{// TODO кастом ошибка обновления
        noteRepository.update(NoteMapper.toNote(noteDto));
    }

    @Override
    public NoteDto getNoteById(int noteId) throws{// TODO кастом ошибка поиска
        return NoteMapper.toNoteDto(noteRepository.findById(noteId));
    }

    @Override
    public List<NoteDto> listNotes(){// вроде нет ошибок тестани
        return noteRepository.findAll().stream().map(NoteMapper::toNoteDto).toList();
    }
}
