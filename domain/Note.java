package domain;

import java.util.ArrayList;
import java.util.List;

import domain.interfaces.TagsInterface;
import domain.interfaces.NoteInterface;

public class Note implements NoteInterface {
    private static int noteCount=0;// не асинхронно
    private final int uidNote;
    private String titleName;
    private String textMain;
    private List<TagsInterface> tagsList;

    public Note(String titleName, String textMain,int uidNote, List<TagsInterface> tagList){
        this.uidNote=uidNote;
        this.titleName=titleName;
        this.textMain=textMain;
        this.tagsList=new ArrayList<>(tagList);
        NoteCount();
    }
    public Note(String titleName, String textMain,List<TagsInterface> tagList){
        this(titleName,textMain,Note.noteCount,tagList);
    }
    private void NoteCount(){
        noteCount++;
        return;
    }
    public Note(String titleName, String textMain){
        this(titleName,textMain,new ArrayList<>());
    }
    public Note(String titleName){
        this(titleName,"empty",new ArrayList<>());
    }
    public Note(String titleName,List<TagsInterface> tags){
        this(titleName,"empty",tags);
    }
    public static int getNoteCount() {
        return noteCount;
    }
    public static void setNoteCount(int noteCount) {
        Note.noteCount = noteCount;
    }
    @Override
    public int getUidNote() {
        return uidNote;
    }
    @Override
    public String getTitleName() {
        return titleName;
    }
    @Override
    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
    @Override
    public String getTextMain() {
        return textMain;
    }
    @Override
    public void setTextMain(String textMain) {
        this.textMain = textMain;
    }
    @Override
    public void setTagsList(List<TagsInterface> tagsList) {
        this.tagsList = new ArrayList<>(tagsList);
    }
    @Override
    public List<TagsInterface> getTagsList(){
        return new ArrayList<>(this.tagsList);
    }

    // public static NoteInterface of(String titleName, String textMain, List<TagsInterface> tagsList) {
    //     return new Note(titleName, textMain, tagsList);
    // }
    // public static NoteInterface of(String titleName, String textMain) {
    //     return new Note(titleName, textMain);
    // }
    // public static NoteInterface of(String titleName, String textMain,int uidNote, List<TagsInterface> tagsList) {
    //     return new Note(titleName, textMain, uidNote, tagsList);
    // }


    @Override
    public String toString() {
        return "Note [uidNote=" + uidNote + ", titleName=" + titleName + ", textMain=" + textMain + ", tagsList="
                + tagsList + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + uidNote;
        result = prime * result + ((titleName == null) ? 0 : titleName.hashCode());
        result = prime * result + ((textMain == null) ? 0 : textMain.hashCode());
        result = prime * result + ((tagsList == null) ? 0 : tagsList.hashCode());
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
        Note other = (Note) obj;
        if (uidNote != other.uidNote)
            return false;
        if (titleName == null) {
            if (other.titleName != null)
                return false;
        } else if (!titleName.equals(other.titleName))
            return false;
        if (textMain == null) {
            if (other.textMain != null)
                return false;
        } else if (!textMain.equals(other.textMain))
            return false;
        if (tagsList == null) {
            if (other.tagsList != null)
                return false;
        } else if (!tagsList.equals(other.tagsList))
            return false;
        return true;
    }
    
}
