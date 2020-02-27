package com.example.txnotes;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.txnotes.db.NotesDatabase;


public class TXNotesApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("DEBUG_DB", "app is started");

        // FIXME fix allowMainThreadQueries() !!!!!!
        NotesDatabase db =  Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class, "notes.db").allowMainThreadQueries().build();
    }
}


