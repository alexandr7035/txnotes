package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditNoteViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public EditNoteViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EditNoteViewModel(application);
    }
}