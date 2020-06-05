package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.db.NotesDao;
import com.alexandr7035.txnotes.db.NotesDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// May be moved to separate file if needed
public class NotesRepository {

    private NotesDao dao;
    private Executor executor;

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

    LiveData<Integer> getNotesCount() {
        return notesCount;
    }

}