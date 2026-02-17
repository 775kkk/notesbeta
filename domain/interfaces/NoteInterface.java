package domain.interfaces;

import java.util.List;

public interface NoteInterface {
    String getTextMain();
    void setTitleName(String titleName);
    int getUidNote();
    String getTitleName();
    void setTextMain(String textMain);
    void setTagsList(List<TagsInterface> tagsList);
    List<TagsInterface> getTagsList();

}
