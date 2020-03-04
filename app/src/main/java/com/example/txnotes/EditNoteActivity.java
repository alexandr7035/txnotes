package com.example.txnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;

public class EditNoteActivity extends AppCompatActivity {

    NotesDatabase db;
    NotesDao db_dao;
    Integer note_id;
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

        // Set old text to textedit field
        final String old_note_text = db_dao.getNoteText(note_id);
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

        // Get modification date and update it in the db
        Long note_updated_date = System.currentTimeMillis() / 1000;
        db_dao.setNoteModificationDate(this.note_id, note_updated_date);

        // Get updated text and set in the db
        String updated_text = note_edit_field.getText().toString();
        db_dao.updateNoteText(this.note_id, updated_text);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }





    public void btnCancelEditNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
