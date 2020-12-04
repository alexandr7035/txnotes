package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexandr7035.txnotes.db.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private NotesRepository repository;

    private LiveData<List<NoteEntity>> notesList;
    private LiveData<Integer> notesCount;
    private MutableLiveData<List<NoteEntity>> selectedNotesLiveData;

    public MainViewModel(Application application){

        repository = new NotesRepository(application);

        // Init data (from repo)
        notesList = repository.getAllNotesFromDb();
        notesCount = repository.getNotesCount();

        selectedNotesLiveData = new MutableLiveData<List<NoteEntity>>(new ArrayList<NoteEntity>());


    }

    public LiveData<List<NoteEntity>> getNotesList()  {
        return notesList;
    }

    public LiveData<Integer> getNotesCount() {
        return notesCount;
    }

    public MutableLiveData<List<NoteEntity>> getSelectedNotesLiveData() {
        return selectedNotesLiveData;
    }


    public void removeNote(NoteEntity note) {
        repository.removeNoteFromDb(note);
    }
}



