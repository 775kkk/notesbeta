package application.ports;

import java.util.List;

import application.exceptions.DuplicateNoteException;
import application.exceptions.NoteNotFoundException;
import application.exceptions.TagNotFoundException;
import application.ports.in.dto.CreateNoteDto;
import application.ports.in.dto.NoteDto;
import application.ports.interfaces.NotesServiceInterface;
import application.ports.out.interfaces.NoteRepositoryInterface;
import application.ports.out.interfaces.TagRepositoryInterface;
import application.ports.out.mappers.NoteMapper;
import application.ports.out.mappers.TagMapper;
import domain.interfaces.TagsInterface;

public class NotesServiceImpl implements NotesServiceInterface {
    private final NoteRepositoryInterface noteRepository;
    private final TagRepositoryInterface tagRepository;
    
    public NotesServiceImpl(NoteRepositoryInterface noteRepository, TagRepositoryInterface tagRepository) {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
    }

//========================NOTE========================
    @Override
    public NoteDto createNote(CreateNoteDto dto) throws DuplicateNoteException{
        return NoteMapper.toNoteDto(noteRepository.save(NoteMapper.toNote(dto, resolveTags(dto.tags()))));
    }

    @Override
    public void deleteNote(int noteId) throws NoteNotFoundException{// TODO кастом ошибка удаления
        noteRepository.delete(noteId);
    }

    @Override
    public void updateNote(NoteDto noteDto) throws NoteNotFoundException{// TODO кастом ошибка обновления
        noteRepository.update(NoteMapper.toNote(noteDto, resolveTags(noteDto.tags())));
    }

    @Override
    public NoteDto getNoteById(int noteId) throws NoteNotFoundException{
        return NoteMapper.toNoteDto(noteRepository.findById(noteId));
    }

    @Override
    public List<NoteDto> listNotes(){// вроде нет ошибок тестани
        return noteRepository.findAll().stream().map(NoteMapper::toNoteDto).toList();
    }

    private List<TagsInterface> resolveTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return List.of();
        }
        return tagNames.stream().map(name -> {
            try {
                return ((TagsInterface) tagRepository.findByName(name));
            } catch (TagNotFoundException e) {
                return tagRepository.save(TagMapper.StringToTag(name));
            }
        }).toList();
    }
}
