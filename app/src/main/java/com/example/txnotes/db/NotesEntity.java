package com.example.txnotes.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "notes")
public class NotesEntity {

    public NotesEntity(String note_text, Integer note_creation_date) {
        this.note_text = note_text;
        this.note_creation_date = note_creation_date;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "note_text")
    public String note_text;

    @ColumnInfo(name = "note_creation_date")
    public Integer note_creation_date;



}
