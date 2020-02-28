package com.example.txnotes;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.txnotes.db.NotesDatabase;


public class TXNotesApplication extends Application {


    NotesDatabase db;

    // FIXME fix allowMainThreadQueries() !!!!!!


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();

        this.db = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class, "notes.db").allowMainThreadQueries().build();

        Log.d("DEBUG_DB", "app is started");

    }

    public NotesDatabase getDatabaseInstance() {
      return(this.db);
    }
}


