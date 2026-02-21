package application.ports.out.mappers;

import java.util.List;

import application.ports.in.dto.CreateTagDto;
import application.ports.in.dto.TagDto;
import domain.Tags;
import domain.interfaces.TagsInterface;

public class TagMapper {
    
    public static TagDto toTagDto(TagsInterface tag) {
        return new TagDto(tag.getTagName(), tag.getUidTag());
    }
    public static TagsInterface toTag(TagDto tagDto){
        return new Tags(tagDto.tagName(), tagDto.uidTag());
    }
    public static TagsInterface toTag(CreateTagDto createTagDto) {
        return new Tags(createTagDto.tagName());
    }
    public static List<TagDto> toTagDtoList(List<TagsInterface> tags) {
        return tags.stream().map(TagMapper::toTagDto).toList();
    }
    public static List<TagsInterface> toTagList(List<TagDto> tagDtos) {
        return tagDtos.stream().map(TagMapper::toTag).toList();
    }
    public static TagsInterface StringToTag(String tagName) {
        return new Tags(tagName);
    }
    public static String toStringTag(TagsInterface tag) {
        return tag.getTagName();
    }

}
