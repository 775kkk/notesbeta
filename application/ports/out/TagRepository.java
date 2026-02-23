package application.ports.out;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import application.exceptions.DuplicateTagException;
import application.exceptions.TagNotFoundException;
import application.ports.out.interfaces.TagRepositoryInterface;
import domain.interfaces.TagsInterface;

public class TagRepository implements TagRepositoryInterface {
    private final HashMap<Integer, TagsInterface> tags = new LinkedHashMap<>();
    private final HashMap<String, Integer> NameToId = new HashMap<>();

     // beta

    @Override
    public TagsInterface save(TagsInterface tag) throws DuplicateTagException{
        if (!tags.containsKey(tag.getUidTag())) {
            tags.put(tag.getUidTag(), tag);
            NameToId.put(tag.getTagName(), tag.getUidTag());
            return tag;
        }
        throw new DuplicateTagException("Tag with ID " + tag.getUidTag() + " already exists");
    }

    @Override
    public void update(TagsInterface tag) throws TagNotFoundException {
        if (tags.containsKey(tag.getUidTag())) {
            NameToId.remove(tags.get(tag.getUidTag()).getTagName());
            NameToId.put(tag.getTagName(), tag.getUidTag());
            tags.put(tag.getUidTag(), tag);
            return;
        }//TODO СДЕЛАТЬ УМНЫЙ АПДЕЙТ
        throw new TagNotFoundException("Failed to update tag with ID " + tag.getUidTag());
    }

    @Override
    public void delete(int tagId) throws TagNotFoundException{// 2 делита разных один идшный другой по имени
        if (!tags.containsKey(tagId)) {
            throw new TagNotFoundException("Failed to delete tag with ID " + tagId);
        }
        NameToId.remove(tags.get(tagId).getTagName());// TODO  точно в отдельный метод и там исключение
        tags.remove(tagId);
    }

    @Override
    public TagsInterface findById(int tagId) throws TagNotFoundException {//TODO
        if (!tags.containsKey(tagId)) {
            throw new TagNotFoundException("Failed to find tag with ID " + tagId);
        }
        return tags.get(tagId);
    }

    public TagsInterface findByName(String tagName) throws TagNotFoundException {
        if (!NameToId.containsKey(tagName)) {
            throw new TagNotFoundException("Failed to find tag with name " + tagName);
        }
        return tags.get(NameToId.get(tagName));
    }

    @Override
    public List<TagsInterface> findAll() {
        if (tags.isEmpty()) {
            return List.of();
        }
        return tags.values().stream().toList();
    }
}