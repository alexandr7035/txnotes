package com.alexandr7035.txnotes.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDao {


    @Query("SELECT COUNT(*) FROM notes")
    int getNotesCount();

    // Reverse by default (show new ones first)
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<NoteEntity> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = (:id)")
    NoteEntity getNoteById(int id);

    @Insert
    void addNote(NoteEntity... notesEntities);

    @Update
    void updateNote(NoteEntity noteEntity);

    @Delete
    void deleteNote(NoteEntity noteEntity);
}