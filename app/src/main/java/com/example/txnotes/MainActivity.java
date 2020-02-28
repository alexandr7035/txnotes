package com.example.txnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;
import com.example.txnotes.TXNotesApplication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB
        NotesDatabase db = ((TXNotesApplication) this.getApplication()).getDatabaseInstance();
        NotesDao db_dao = db.getNotesDao();

        // Get notes count and add to title
        TextView app_title = findViewById(R.id.appTitleView);
        //Integer notes_count = 5;
        Integer notes_count = db_dao.getNotesCount();
        app_title.setText(getString(R.string.app_title,  " (" + notes_count + ")"));

        // A layout for notes
        LinearLayout notes_layout = findViewById(R.id.notesLayout);
        for (Integer i=notes_count; i>=1; i--) {
            Button note_btn = new Button(getApplicationContext());
            String note_text = db_dao.getNoteText(i);
            note_btn.setText(note_text);
            notes_layout.addView(note_btn);
        }

    }

    // Adds a new note and shows NewNoteActivity
    public void createNewNote(View view) {
        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        startActivity(intent);
    }


}
