package com.example.txnotes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;
import com.example.txnotes.db.NoteEntity;

public class EditNoteActivity extends AppCompatActivity {

    NotesDatabase db;
    NotesDao db_dao;
    NoteEntity note_data;
    int note_id;
    EditText note_edit_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // DB
        db = ((TXNotesApplication) this.getApplication()).getDatabaseInstance();
        db_dao = db.getNotesDao();

        // Get note_id
        Intent intent = getIntent();
        note_id = intent.getIntExtra("edited_note_id", 0);

        // Get note's entity
        this.note_data = db_dao.getNoteById(note_id);

        // Set old text to textedit field
        final String old_note_text = this.note_data.getNoteText();
        note_edit_field = findViewById(R.id.editedNoteTextField);
        note_edit_field.setText(old_note_text);

        // Save btn object
        final ImageButton save_edited_note_btn = findViewById(R.id.saveEditedNoteBtn);
        save_edited_note_btn.setEnabled(false);

        // Add change listener for noteText
        note_edit_field.addTextChangedListener(new TextWatcher() {

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
                if (text.trim().length() > 0 && !text.trim().equals(old_note_text) ){
                    save_edited_note_btn.setEnabled(true);
                }
                else {
                    save_edited_note_btn.setEnabled(false);
                }

            }
        });

    }

    public void btnSaveEditedNote(View view) {

        // FIXME
        // Get modification date and update it in the db
        Long note_updated_date = System.currentTimeMillis() / 1000;
        note_data.setNoteModificationDate(note_updated_date);

        // FIXME
        // Get updated text and set in the db
        String updated_text = note_edit_field.getText().toString();
        note_data.setNoteText(updated_text);

        // Update NoteEntity
        db_dao.updateNote(note_data);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }





    public void btnCancelEditNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
