package com.example.txnotes.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {


    @Query("SELECT COUNT(*) FROM notes")
    Integer getNotesCount();

    @Query("SELECT * FROM notes")
    List<NotesEntity> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = (:id)")
    NotesEntity getNoteById(int id);
    @Insert
    void addNote(NotesEntity... notesEntities);

    //@Delete
    //void delete(User user);
}