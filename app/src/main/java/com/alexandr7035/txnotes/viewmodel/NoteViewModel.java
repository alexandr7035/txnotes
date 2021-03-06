package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.alexandr7035.txnotes.db.NoteEntity;

import java.util.concurrent.ExecutionException;

public class NoteViewModel extends ViewModel {
    private NotesRepository repository;
    private NoteEntity note;

    public NoteViewModel(Application application) {
        repository = new NotesRepository(application);
    }

    public NoteEntity getNote(long id) throws ExecutionException, InterruptedException {
        return repository.getNoteFromDb(id);
    }

    public long createNote(NoteEntity note) throws ExecutionException, InterruptedException {
        return repository.addNoteToDb(note);
    }

    public void updateNote(NoteEntity note) {
        repository.updateNoteInDb(note);
    }

}
