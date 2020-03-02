package com.example.txnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // DB
        NotesDatabase db = ((TXNotesApplication) this.getApplication()).getDatabaseInstance();
        NotesDao db_dao = db.getNotesDao();

        // Get note_id
        Intent intent = getIntent();
        Integer note_id = intent.getIntExtra("edited_note_id", 0);

        // Get textedit object and set old text
        EditText note_edit = findViewById(R.id.editedNoteTextField);
        String old_note_text = db_dao.getNoteText(note_id);
        note_edit.setText(old_note_text);

    }

    public void btnSaveEditedNote(View view) {

    }

    public void btnCancelEditNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
