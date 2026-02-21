package domain.interfaces;

import java.util.List;

public interface NoteInterface {
    public String getTextMain();
    public void setTitleName(String titleName);
    public int getUidNote();
    public String getTitleName();
    public void setTextMain(String textMain);
    public void setTagsList(List<TagsInterface> tagsList);
    public List<TagsInterface> getTagsList();

}
