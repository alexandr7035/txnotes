package com.alexandr7035.txnotes.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// FIXME export schema
@Database(entities = {NoteEntity.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao getNotesDao();
}