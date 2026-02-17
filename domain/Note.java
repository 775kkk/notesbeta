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

    public Note(String titleName, String textMain, List<TagsInterface> tagList){// 16:30 цтс
        this.uidNote=Note.noteCount;
        this.titleName=titleName;
        this.textMain=textMain;
        this.tagsList=new ArrayList<>(tagList);
        NoteCount();
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
}
