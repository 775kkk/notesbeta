package domain;

import domain.interfaces.TagsInterface;

public class Tags implements TagsInterface{
    private String tagName;
    private int uidTag;
    private static int tagCount=0;// не асинхронно

    public Tags(String tagName){
        this.tagName=tagName;
        this.uidTag=Tags.tagCount;
        TagsCount();
    }
    private void TagsCount(){
        tagCount++;
        return;
    }
    public static int getTagCount() {
        return tagCount;
    }
    @Override
    public String getTagName() {
        return tagName;
    }
    public int getUidTag() {
        return uidTag;
    }
}
