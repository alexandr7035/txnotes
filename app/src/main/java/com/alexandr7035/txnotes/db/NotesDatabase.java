package com.alexandr7035.txnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// FIXME export schema
@Database(entities = {NoteEntity.class}, version = 2, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao getNotesDao();

    private static NotesDatabase INSTANCE;

    // Use singleton approach
    public static NotesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NotesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotesDatabase.class, "notes.db")
                            .addMigrations(NotesDatabase.MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE notes ADD COLUMN note_title TEXT");

            Cursor cursor = database.query("SELECT * FROM notes");

            while(cursor.moveToNext()) {

                long id = cursor.getLong(cursor.getColumnIndex("id"));

                // Get first 30 symbols from text and set as title
                String text = cursor.getString(cursor.getColumnIndex("note_text"));
                String title = text.trim().substring(0, Math.min(text.length(), 30)).trim();

                String query = "UPDATE notes SET note_title = '" + title + "' WHERE id = '" + id + "';";
                Log.d("DEBUG_TXNOTES", query);
                
                database.execSQL(query);
            }

        }
    };
}

