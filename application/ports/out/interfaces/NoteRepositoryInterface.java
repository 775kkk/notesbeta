package application.ports.out.interfaces;

import java.util.List;

import domain.interfaces.NoteInterface;

public interface NoteRepositoryInterface {// интерфейс адаптера

    // beta
    public void add(NoteInterface note);
    public void update(NoteInterface note);
    public void delete(int noteId);
    public NoteInterface findById(int noteId);
    public List<NoteInterface> findAll();

}