import javax.swing.SwingUtilities;

import application.ports.NotesServiceImpl;
import application.ports.TagsServiceImpl;
import application.ports.interfaces.NotesServiceInterface;
import application.ports.interfaces.TagsServiceInterface;
import application.ports.out.NoteRepository;
import application.ports.out.TagRepository;
import application.ports.out.interfaces.NoteRepositoryInterface;
import application.ports.out.interfaces.TagRepositoryInterface;
import presentation.view.NotesFrame;
import presentation.viewmodel.NotesViewModel;

public class Main {

    public static void main(String[] args) {
        // Composition root
        TagRepositoryInterface tagRepository = new TagRepository();
        NoteRepositoryInterface noteRepository = new NoteRepository();

        TagsServiceInterface tagsService = new TagsServiceImpl(tagRepository);
        NotesServiceInterface notesService = new NotesServiceImpl(noteRepository, tagRepository);

        // tagsService is kept for future separate tags screen
        // (current UI uses only NotesViewModel)
        NotesViewModel notesViewModel = new NotesViewModel(notesService);

        SwingUtilities.invokeLater(() -> {
            NotesFrame frame = new NotesFrame(notesViewModel);
            frame.setVisible(true);
        });
    }
}
