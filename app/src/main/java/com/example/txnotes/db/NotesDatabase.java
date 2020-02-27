package com.example.txnotes.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.txnotes.db.NotesEntity;

@Database(entities = {NotesEntity.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao getNotesDao();
}