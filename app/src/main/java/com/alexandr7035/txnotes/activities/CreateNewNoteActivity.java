package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.alexandr7035.txnotes.R;


public class CreateNewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_note);

        // Set activity's title (new note)
        String title_string;
        title_string = getString(R.string.activity_create_note_title);
        setTitle(title_string);

        // Elements' objects to operate
        final EditText note_text = findViewById(R.id.noteTextField);
        final ImageButton save_btn = findViewById(R.id.saveNoteBtn);

        // Disable 'save' button first
        save_btn.setEnabled(false);

        // Add change listener for noteText
        note_text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                // Set hint text to the noteTitle field
                // If user doesn't specify the title. it will be taken from the
                // first 10 characters of the main text
                ;;;;;;;;;;;;;;;;;;;;;;

                String text = s.toString();

                // Disable saveNote button if noteText is empty
                if (text.trim().length() > 0) {
                    save_btn.setEnabled(true);
                }
                else {
                    save_btn.setEnabled(false);
                }

            }
        });

    }

    // Gets text from fields (title and text) and adds to the db
    public void btnAddNote(View view) {

        EditText text;

        // Get note text from 'noteText' element
        text = findViewById(R.id.noteTextField);
        String note_text = text.getText().toString();

        long note_creation_date = System.currentTimeMillis() / 1000;

        // Add note to db
        //NotesDatabase db = ((TXNotesApplication) this.getApplication()).getDatabaseInstance();
        //NotesDao db_dao = db.getNotesDao();

        //db_dao.addNote(new NoteEntity(note_text, note_creation_date));

        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Back button in CreateNewNoteActivity (sends to main menu)
    public void btnCancelNewNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
