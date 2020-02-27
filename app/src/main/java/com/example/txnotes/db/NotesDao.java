package com.example.txnotes.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT note_text FROM notes WHERE id = (:id)")
    String getNoteText(Integer id);

    @Query("SELECT COUNT(*) FROM notes")
    Integer getNotesCount();

    @Insert
    void addNote(NotesEntity noteEntity);

    //@Delete
    //void delete(User user);
}