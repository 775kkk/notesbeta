package application.ports.out.interfaces;

import java.util.List;

import application.ports.in.dto.CreateTagDto;
import application.ports.in.dto.TagDto;

public interface TagRepositoryInterface {
    public void add(CreateTagDto tag);
    public void update(TagDto tag);
    public void delete(int tagId);
    public TagDto findById(int tagId);
    public List<TagDto> findAll();
}
