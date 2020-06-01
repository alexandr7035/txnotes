package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alexandr7035.txnotes.db.NoteEntity;

import java.util.List;

public class MainViewModel extends ViewModel {

    private LiveData<List<NoteEntity>> notesList;
    private NotesRepository repository;

    public MainViewModel(Application application){

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



