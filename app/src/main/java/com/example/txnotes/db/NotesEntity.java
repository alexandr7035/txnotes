package com.example.txnotes.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "notes")
public class NotesEntity {

    public NotesEntity(String note_text, Long note_creation_date) {
        this.note_text = note_text;
        this.note_creation_date = note_creation_date;
        this.note_modification_date = new Long(0);
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "note_text")
    public String note_text;

    @ColumnInfo(name = "note_creation_date")
    public Long note_creation_date;

    @ColumnInfo(name = "note_modification_date")
    public Long note_modification_date;

    public Long getNoteCreationDate() {
        return this.note_creation_date;
    }

    public Long getNoteModificationDate() {
        return this.note_modification_date;
    }

    public void setNoteModificationDate(Long date) {
        this.note_modification_date = date;
    }

    public String getNoteText() {
        return this.note_text;
    }

    public void setNoteText(String text) {
        this.note_text = text;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
