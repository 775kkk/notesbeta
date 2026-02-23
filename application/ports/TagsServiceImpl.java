package application.ports;

import java.util.List;

import application.ports.in.dto.CreateTagDto;
import application.ports.in.dto.TagDto;
import application.ports.interfaces.TagsServiceInterface;
import application.ports.out.interfaces.TagRepositoryInterface;
import application.ports.out.mappers.TagMapper;

public class TagsServiceImpl implements TagsServiceInterface{
    
    private final TagRepositoryInterface tagRepository;


    public TagsServiceImpl(TagRepositoryInterface tagRepository) {
        this.tagRepository = tagRepository;
    }
    //beta
    //========================TAG========================

    @Override
    public TagDto createTag(CreateTagDto dto) throws {// TODO кастом ошибка если тэг с таким id есть
        return TagMapper.toTagDto(tagRepository.save(TagMapper.toTag(dto)));
    }

    @Override
    public void deleteTag(int tagId) throws {// TODO кастом ошибка удаления
        tagRepository.delete(tagId);
    }

    @Override
    public void updateTag(TagDto tagDto) throws {// TODO кастом ошибка обновления
        tagRepository.update(TagMapper.toTag(tagDto));
    }

    @Override
    public TagDto getTagById(int tagId) throws {// TODO кастом ошибка поиска
        return TagMapper.toTagDto(tagRepository.findById(tagId));
    }

    @Override
    public List<TagDto> listTags(){// вроде нет ошибок тестани
        return tagRepository.findAll().stream().map(TagMapper::toTagDto).toList();
    }

}