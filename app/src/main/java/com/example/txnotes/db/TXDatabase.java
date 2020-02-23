package com.example.txnotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TXDatabase extends SQLiteOpenHelper {

    // Constructor
    public TXDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "txnotes.db";

    // 1 table in the DB
    // Table's schema
    final String TABLE_NAME = "notes";


    private final String SQL_CREATE_NOTES_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, note_title TEXT, note_text TEXT, creation_date INTEGER, last_change_date INTEGER)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Adds new note record to the database
    public void addNote(String note_title, String note_text,
                 long creation_date, long last_change_date) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("note_title", note_title);
        values.put("note_text", note_text);
        values.put("creation_date", creation_date);
        values.put("last_change_date", last_change_date);

        // Insert the new row, returning the primary key value of the new row
        db.insert("notes", null, values);

        db.close();
    }
}
