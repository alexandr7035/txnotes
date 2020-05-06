package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.db.NotesDao;
import com.alexandr7035.txnotes.db.NotesDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<NoteEntity>> notesList;
    private NotesRepository repository;

    public MainViewModel(Application application){
        super(application);

        repository = new NotesRepository(application);

        notesList = repository.getAllNotesFromDb();
    }

    public LiveData<List<NoteEntity>> getNotesList()  {
        return notesList;
    }

    public void addNote(NoteEntity note) {
        repository.addNoteToDb(note);
    }

    public void removeNote(NoteEntity note) {
        repository.removeNoteFromDb(note);
    }
}


// May be moved to separate file if needed
class NotesRepository {

    private NotesDao dao;
    private Executor executor;
    private LiveData<List<NoteEntity>> notesList;

    NotesRepository(Application application) {
        dao = NotesDatabase.getDatabase(application).getNotesDao();

        // To run background tasks
        executor = Executors.newSingleThreadExecutor();

        notesList = dao.getAllNotes();
    }

    void addNoteToDb(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.addNote(note);
            }
        });
    }

    void removeNoteFromDb(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteNote(note);
            }
        });
    }

    LiveData<List<NoteEntity>> getAllNotesFromDb() {
        return notesList;
    }

}
