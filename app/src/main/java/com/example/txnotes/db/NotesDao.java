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

    @Query("SELECT note_creation_date FROM notes WHERE id = (:id)")
    Long getNoteCreationDate(Integer id);

    @Query("SELECT note_modification_date FROM notes WHERE id = (:id)")
    Long getNoteModificationDate(Integer id);

    @Query("UPDATE notes SET note_text = (:note_text) WHERE id = (:id)")
    void updateNoteText(Integer id, String note_text);

    @Query("UPDATE notes SET note_modification_date = (:date) WHERE id = (:id)")
    void setNoteModificationDate(Integer id, Long date);

    @Insert
    void addNote(NotesEntity noteEntity);

    //@Delete
    //void delete(User user);
}