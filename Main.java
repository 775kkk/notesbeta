import application.ports.NotesServiceImpl;
import application.ports.TagsServiceImpl;
import application.ports.interfaces.NotesServiceInterface;
import application.ports.interfaces.TagsServiceInterface;
import application.ports.out.NoteRepository;
import application.ports.out.TagRepository;
import application.ports.out.interfaces.NoteRepositoryInterface;
import application.ports.out.interfaces.TagRepositoryInterface;

public class Main {

    public static void main(String[] args) {
        TagRepositoryInterface tagRepository = new TagRepository();
        NoteRepositoryInterface noteRepository = new NoteRepository();

        // services
        TagsServiceInterface tagsService = new TagsServiceImpl(tagRepository);
        NotesServiceInterface notesService = new NotesServiceImpl(noteRepository, tagRepository);
        
        // дальше сюда подключишь ViewModel / UI
        // NotesViewModel notesVm = new NotesViewModel(notesService);
        // TagsViewModel tagsVm = new TagsViewModel(tagsService);
        // new MainFrame(notesVm, tagsVm).setVisible(true);

        System.out.println("Services initialized successfully.");
    }
}
