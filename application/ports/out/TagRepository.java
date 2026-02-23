package application.ports.out;

import java.util.HashMap;
import java.util.List;

import application.ports.out.interfaces.TagRepositoryInterface;
import domain.interfaces.TagsInterface;

public class TagRepository implements TagRepositoryInterface {
    private final HashMap<Integer, TagsInterface> tags = new HashMap<>();

    @Override
    public TagsInterface save(TagsInterface tag) throws {//TODO
        if (!tags.containsKey(tag.getUidTag())) {
            return tags.put(tag.getUidTag(), tag);
        }
        throw new //TODO тут кастом ошибка что тэг с таким id уже существует
    }

    @Override
    public void update(TagsInterface tag) throws {//TODO
        if (tags.containsKey(tag.getUidTag())) {
            tags.put(tag.getUidTag(), tag);
            return;
        }
        throw new //TODO тут кастом ошибка неудачи метода
    }

    @Override
    public void delete(int tagId) throws {//TODO
        if (!tags.containsKey(tagId)) {
            throw new //TODO кастом ошибка что тэг с таким id не существует
        }
        tags.remove(tagId);
    }

    @Override
    public TagsInterface findById(int tagId) throws {//TODO
        if (!tags.containsKey(tagId)) {
            throw new //TODO кастом ошибка что тэг с таким id не существует
        }
        return tags.get(tagId);
    }

    @Override
    public List<TagsInterface> findAll() {
        if (tags.isEmpty()) {
            return List.of();
        }
        return tags.values().stream().toList();
    }
}