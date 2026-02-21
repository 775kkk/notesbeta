package application.ports.out.interfaces;

import java.util.List;

import domain.interfaces.TagsInterface;

// import application.ports.in.dto.CreateTagDto;
// import application.ports.in.dto.TagDto;

public interface TagRepositoryInterface {
    public void add(TagsInterface tag);
    public void update(TagsInterface tag);
    public void delete(int tagId);
    public TagsInterface findById(int tagId);
    public List<TagsInterface> findAll();
}
