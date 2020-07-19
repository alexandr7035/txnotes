package com.alexandr7035.txnotes.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ShowNoteViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    public ShowNoteViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ShowNoteViewModel(application);
    }
}
