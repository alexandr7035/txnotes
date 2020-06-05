package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alexandr7035.txnotes.db.NoteEntity;

import java.util.List;

public class MainViewModel extends ViewModel {

    private NotesRepository repository;

    private LiveData<List<NoteEntity>> notesList;
    private LiveData<Integer> notesCount;

    public MainViewModel(Application application){

        repository = new NotesRepository(application);

        // Init data (from repo)
        notesList = repository.getAllNotesFromDb();
        notesCount = repository.getNotesCount();
    }

    public LiveData<List<NoteEntity>> getNotesList()  {
        return notesList;
    }

    public LiveData<Integer> getNotesCount() {
        return notesCount;
    }

    public void addNote(NoteEntity note) {
        repository.addNoteToDb(note);
    }

    public void removeNote(NoteEntity note) {
        repository.removeNoteFromDb(note);
    }
}



