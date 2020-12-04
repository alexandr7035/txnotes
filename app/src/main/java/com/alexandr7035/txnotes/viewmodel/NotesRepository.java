package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.db.NotesDao;
import com.alexandr7035.txnotes.db.NotesDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// May be moved to separate file if needed
public class NotesRepository {

    private NotesDao dao;
    private ExecutorService executor;

    private LiveData<List<NoteEntity>> notesList;
    private LiveData<Integer> notesCount;

    NotesRepository(Application application) {

        NotesDatabase db = NotesDatabase.getDatabase(application);

        dao = db.getNotesDao();

        // To run background tasks
        executor = Executors.newSingleThreadExecutor();

        Log.d("DEBUG_TXNOTES", "all notes " + dao.getAllNotes().getValue());

        // Init data
        notesList = dao.getAllNotes();
        notesCount = dao.getNotesCount();

    }

    long addNoteToDb(final NoteEntity note) throws ExecutionException, InterruptedException {

        Future foo = executor.submit(new Callable() {
            @Override
            public Long call() {
                return dao.addNote(note);
            }
        });

        return (Long) foo.get();

    }

    void removeNoteFromDb(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteNote(note);
            }
        });
    }

    void updateNoteInDb(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateNote(note);
            }
        });
    }

    LiveData<List<NoteEntity>> getAllNotesFromDb() {
        return notesList;
    }

    LiveData<Integer> getNotesCount() {
        return notesCount;
    }

    NoteEntity getNoteFromDb(final int id) throws ExecutionException, InterruptedException {
        Future foo = executor.submit(new Callable() {
            @Override
            public NoteEntity call() {
                return dao.getNoteById(id);
            }
        });


        return (NoteEntity) foo.get();
    }

    MutableLiveData<NoteEntity> getNoteLiveDataFromDb(Long id) {
        MutableLiveData<NoteEntity> data = (MutableLiveData<NoteEntity>) dao.getNoteLiveDataById(id);

        return data;
    }

}