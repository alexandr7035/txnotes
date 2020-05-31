package com.alexandr7035.txnotes;

import android.app.Application;
import android.util.Log;


public class TXNotesApplication extends Application {

    private final String LOG_TAG = "DEBUG_TXNOTES";

    // FIXME fix allowMainThreadQueries() !!!!!!
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();

        //this.db = Room.databaseBuilder(getApplicationContext(),
        //        NotesDatabase.class, "notes.db").allowMainThreadQueries().build();

        Log.d(LOG_TAG, "the app has been started now");

    }

    //public NotesDatabase getDatabaseInstance() {
    //  return(this.db);
    //}

    public String getLogTag() {
        return this.LOG_TAG;
    }
}


