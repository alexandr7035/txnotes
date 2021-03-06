package com.alexandr7035.txnotes.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "notes")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long id;

    @ColumnInfo(name = "note_text")
    public String note_text;

    @ColumnInfo(name = "note_creation_date")
    public long note_creation_date;

    @ColumnInfo(name = "note_modification_date")
    public long note_modification_date;

    @ColumnInfo(name = "note_title")
    public String note_title;

    public long getNoteCreationDate() {
        return this.note_creation_date;
    }

    public void setNoteCreationDate(long date) {
        this.note_creation_date = date;
    }

    public long getNoteModificationDate() {
        return this.note_modification_date;
    }

    public void setNoteModificationDate(long date) {
        this.note_modification_date = date;
    }

    public String getNoteText() {
        return this.note_text;
    }

    public void setNoteText(String text) {
        this.note_text = text;
    }

    public String getNoteTitle() {
        return this.note_title;
    }

    public void setNoteTitle(String text) {
        this.note_title = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
