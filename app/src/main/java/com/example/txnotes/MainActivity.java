package com.example.txnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;

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
        app_title.setText(getString(R.string.app_title, " (" + notes_count + ")"));

        // A layout for notes
        LinearLayout notes_layout = findViewById(R.id.notesLayout);
        for (Integer i = notes_count; i >= 1; i--) {
            // Create note button
            NoteWidget note_btn = new NoteWidget(getApplicationContext());

            // Set title
            String note_text = db_dao.getNoteText(i);
            note_btn.setNoteTitle(note_text);

            // Set date
            Long date = db_dao.getNoteCreationDate(i);
            note_btn.setNoteDate(date);

            // Add note to the notesLayout
            notes_layout.addView(note_btn);

            // Set onclick function
            note_btn.setOnClickListener(new NoteClickListener(i));

        }

    }

    // Adds a new note and shows NewNoteActivity
    public void createNewNote(View view) {
        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        startActivity(intent);
    }


    // Click listener for notes
    public class NoteClickListener implements View.OnClickListener {

        int note_id;

        public NoteClickListener(int note_id) {
            this.note_id = note_id;
        }

        @Override
        public void onClick(View v) {
            Log.i("DEBUG_TX", "clicked " + this.note_id + " note");

            // Go to CreateNewNoteActivity
            Intent intent = new Intent(v.getContext(), ShowNoteActivity.class);
            intent.putExtra("clicked_note_id", this.note_id);
            startActivity(intent);
        }

    }


    // Disable back button in MainActivity
    @Override
    public void onBackPressed() {
        // Do nothing
    }

}