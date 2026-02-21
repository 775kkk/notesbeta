package application.ports.out;

import java.util.HashMap;
import java.util.List;

import application.ports.out.interfaces.TagRepositoryInterface;
import domain.interfaces.TagsInterface;

public class TagRepository implements TagRepositoryInterface {
    private final HashMap<Integer, TagsInterface> tags = new HashMap<>();

    @Override
    public void add(TagsInterface tag) {
        if (!tags.containsKey(tag.getUidTag())) {
            tags.put(tag.getUidTag(), tag);
        }
    }

    @Override
    public void update(TagsInterface tag) {
        if (tags.containsKey(tag.getUidTag())) {
            tags.put(tag.getUidTag(), tag);
        }
    }

    @Override
    public void delete(int tagId) {
        tags.remove(tagId);
    }

    @Override
    public TagsInterface findById(int tagId) {
        return tags.get(tagId);
    }

    @Override
    public List<TagsInterface> findAll() {
        return tags.values().stream().toList();
    }
}