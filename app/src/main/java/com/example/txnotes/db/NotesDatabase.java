package com.example.txnotes.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.txnotes.db.NoteEntity;

// FIXME export schema
@Database(entities = {NoteEntity.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao getNotesDao();
}