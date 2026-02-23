package application.ports.out.interfaces;

import java.util.List;

import domain.interfaces.TagsInterface;

public interface TagRepositoryInterface {
    public TagsInterface save(TagsInterface tag);
    public void update(TagsInterface tag);
    public void delete(int tagId);
    public TagsInterface findById(int tagId);
    public List<TagsInterface> findAll();
    public TagsInterface findByName(String name);
}
