package application.ports.interfaces;

import java.util.List;

import application.ports.in.dto.CreateTagDto;
import application.ports.in.dto.TagDto;

public interface TagsServiceInterface {// интерфейс тегов

    //beta
    public TagDto createTag(CreateTagDto dto);
    public void deleteTag(int tagId);
    public void updateTag(TagDto tagDto);
    public TagDto getTagById(int tagId);
    public List<TagDto> listTags();
    
}
