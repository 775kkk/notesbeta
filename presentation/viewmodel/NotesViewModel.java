package presentation.viewmodel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import application.exceptions.AppException;
import application.ports.in.dto.CreateNoteDto;
import application.ports.in.dto.NoteDto;
import application.ports.interfaces.NotesServiceInterface;

public class NotesViewModel {
    public enum Mode {
        LIST,
        DETAIL
    }

    private final NotesServiceInterface notesService;

    // состояние экрана - фронта
    private Mode mode = Mode.LIST;
    private List<NoteDto> notesList = new ArrayList<>();
    private Integer selectedNoteId;

    // состояние записей
    private String draftTitle = "";
    private String draftContent = "";
    private List<String> draftTags = new ArrayList<>();
    private boolean isNewDraft;
    private boolean dirty;

    // сообщения для фронт фор юзер
    private String statusMessage;
    private String errorMessage;

    public NotesViewModel(NotesServiceInterface notesService) {
        this.notesService = notesService;
    }

    // события от пользователя (жизненного цикла экрана)
    public void loadNotes() {
        clearMessages();
        try {
            this.notesList = new ArrayList<>(notesService.listNotes());
        } catch (AppException e) {
            this.errorMessage = e.getMessage();
        }
    }

    public void openNote(int noteId) {
        clearMessages();
        try {
            NoteDto note = notesService.getNoteById(noteId);
            applyNoteToDraft(note);
            this.selectedNoteId = noteId;
            this.isNewDraft = false;
            this.dirty = false;
            this.mode = Mode.DETAIL;
        } catch (AppException e) {
            this.errorMessage = e.getMessage();
        }
    }

    public void startCreateNote() {
        clearMessages();
        this.mode = Mode.DETAIL;
        this.selectedNoteId = null;
        this.isNewDraft = true;
        this.dirty = false;
        this.draftTitle = "";
        this.draftContent = "";
        this.draftTags = new ArrayList<>();
    }

    public void changeTitle(String newTitle) {
        if (!hasDraft()) {
            return;
        }
        String safeTitle = newTitle == null ? "" : newTitle;
        if (Objects.equals(this.draftTitle, safeTitle)) {
            return;
        }
        this.draftTitle = safeTitle;
        this.dirty = true;
    }

    public void changeContent(String newContent) {
        if (!hasDraft()) {
            return;
        }
        String safeContent = newContent == null ? "" : newContent;
        if (Objects.equals(this.draftContent, safeContent)) {
            return;
        }
        this.draftContent = safeContent;
        this.dirty = true;
    }

    public void changeTags(List<String> newTags) {
        if (!hasDraft()) {
            return;
        }
        List<String> normalized = normalizeTags(newTags);
        if (Objects.equals(this.draftTags, normalized)) {
            return;
        }
        this.draftTags = normalized;
        this.dirty = true;
    }

    public void save() {
        clearMessages();

        if (this.mode != Mode.DETAIL) {
            this.statusMessage = "Nothing to save";
            return;
        }

        if (!this.dirty) {
            this.statusMessage = "No changes";
            return;
        }

        if (!this.isNewDraft && this.selectedNoteId == null) {
            this.errorMessage = "No selected note for update";
            return;
        }

        try {
            if (this.isNewDraft) {
                NoteDto created = notesService.createNote(buildCreateDtoFromDraft());
                applyNoteToDraft(created);
                this.selectedNoteId = created.noteId();
                this.isNewDraft = false;
            } else {
                notesService.updateNote(buildUpdateDtoFromDraft());
                NoteDto updated = notesService.getNoteById(this.selectedNoteId);
                applyNoteToDraft(updated);
            }

            this.dirty = false;
            this.statusMessage = "Saved";
            refreshNotesListSilently();
        } catch (AppException e) {
            this.errorMessage = e.getMessage();
        }
    }

    public void deleteNote(int noteId) {
        clearMessages();
        try {
            notesService.deleteNote(noteId);
            refreshNotesListSilently();

            if (this.selectedNoteId != null && this.selectedNoteId == noteId) {
                resetDetailStateToList();
            }

            this.statusMessage = "Deleted";
        } catch (AppException e) {
            this.errorMessage = e.getMessage();
        }
    }

    public void back() {
        clearMessages();

        if (this.mode == Mode.LIST) {
            return;
        }

        if (this.dirty) {
            save();
            if (this.errorMessage != null) {
                return;
            }
        }

        resetDetailStateToList();
    }

    // Private helpers
    private void clearMessages() {
        this.statusMessage = null;
        this.errorMessage = null;
    }

    private boolean hasDraft() {
        return this.mode == Mode.DETAIL;
    }

    private void applyNoteToDraft(NoteDto note) {
        this.draftTitle = note.title() == null ? "" : note.title();
        this.draftContent = note.content() == null ? "" : note.content();
        this.draftTags = normalizeTags(note.tags());
    }

    private CreateNoteDto buildCreateDtoFromDraft() {
        return new CreateNoteDto(
                normalizeTitle(this.draftTitle),
                normalizeContent(this.draftContent),
                normalizeTags(this.draftTags));
    }

    private NoteDto buildUpdateDtoFromDraft() {
        if (this.selectedNoteId == null) {
            throw new IllegalStateException("selectedNoteId is null for update");
        }
        return new NoteDto(
                this.selectedNoteId,
                normalizeTitle(this.draftTitle),
                normalizeContent(this.draftContent),
                normalizeTags(this.draftTags));
    }

    private void resetDetailStateToList() {
        this.mode = Mode.LIST;
        this.selectedNoteId = null;
        this.draftTitle = "";
        this.draftContent = "";
        this.draftTags = new ArrayList<>();
        this.isNewDraft = false;
        this.dirty = false;
    }

    private void refreshNotesListSilently() {
        this.notesList = new ArrayList<>(notesService.listNotes());
    }

    private String normalizeTitle(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeContent(String value) {
        return value == null ? "" : value;
    }

    private List<String> normalizeTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }

        LinkedHashSet<String> unique = new LinkedHashSet<>();
        for (String tag : tags) {
            if (tag == null) {
                continue;
            }
            String normalized = tag.trim();
            if (!normalized.isEmpty()) {
                unique.add(normalized);
            }
        }
        return new ArrayList<>(unique);
    }

    // Read-only state accessors for View
    public Mode getMode() {
        return mode;
    }

    public List<NoteDto> getNotesList() {
        return new ArrayList<>(notesList);
    }

    public Integer getSelectedNoteId() {
        return selectedNoteId;
    }

    public String getDraftTitle() {
        return draftTitle;
    }

    public String getDraftContent() {
        return draftContent;
    }

    public List<String> getDraftTags() {
        return new ArrayList<>(draftTags);
    }

    public boolean isNewDraft() {
        return isNewDraft;
    }

    public boolean isDirty() {
        return dirty;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
