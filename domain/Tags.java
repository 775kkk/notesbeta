package domain;

import domain.interfaces.TagsInterface;

public class Tags implements TagsInterface{
    private String tagName;
    private int uidTag;
    private static int tagCount=0;// не асинхронно

    public Tags(String tagName){
        this.tagName=tagName;
        this.uidTag=tagName.hashCode();//Tags.tagCount
        TagsCount();
    }

    public Tags(String tagName, int uidTag){
        this.tagName=tagName;
        this.uidTag=uidTag;
        TagsCount();
    }

    // public static TagsInterface of(String tagName, int uidTag) {
    //     return new Tags(tagName, uidTag);
    // }

    // public static TagsInterface of(String tagName) {
    //     return new Tags(tagName);
    // }

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


    @Override
    public String toString() {
        return "Tag [tagName=" + tagName + ", uidTag=" + uidTag + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
        result = prime * result + uidTag;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tags other = (Tags) obj;
        if (tagName == null) {
            if (other.tagName != null)
                return false;
        } else if (!tagName.equals(other.tagName))
            return false;
        if (uidTag != other.uidTag)
            return false;
        return true;
    }


}
