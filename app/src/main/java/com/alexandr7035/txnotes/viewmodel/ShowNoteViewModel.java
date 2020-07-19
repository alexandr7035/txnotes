package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.alexandr7035.txnotes.db.NoteEntity;

import java.util.concurrent.ExecutionException;

public class ShowNoteViewModel extends ViewModel {

    private NotesRepository repository;
    private NoteEntity note;

    public ShowNoteViewModel(Application application) {
        repository = new NotesRepository(application);
    }

    public NoteEntity getNote(int id) throws ExecutionException, InterruptedException {
        return repository.getNoteFromDb(id);
    }
}
