package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexandr7035.txnotes.R;
import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.viewmodel.EditNoteViewModel;
import com.alexandr7035.txnotes.viewmodel.EditNoteViewModelFactory;

import java.util.concurrent.ExecutionException;

public class EditNoteActivity extends AppCompatActivity {

    //NotesDatabase db;
    //NotesDao db_dao;
    NoteEntity note;
    int note_id;
    EditText note_edit_field;
    private EditNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // ViewModel
        viewModel = new ViewModelProvider(this, new EditNoteViewModelFactory(this.getApplication())).get(EditNoteViewModel.class);

        // Get note_id
        Intent intent = getIntent();
        note_id = intent.getIntExtra("edited_note_id", 0);

        // Get edited note
        try {
            this.note = viewModel.getNote(note_id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Set old text to textedit field
        final String old_note_text = this.note.getNoteText();
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
        long note_updated_date = System.currentTimeMillis() / 1000;
        note.setNoteModificationDate(note_updated_date);

        // Get updated text and set in the db
        String updated_text = note_edit_field.getText().toString();
        note.setNoteText(updated_text);

        // Update note
        viewModel.updateNote(note);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void btnCancelEditNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
