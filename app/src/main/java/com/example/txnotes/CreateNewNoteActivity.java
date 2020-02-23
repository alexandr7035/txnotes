package com.example.txnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.txnotes.db.TXDatabase;

public class CreateNewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_note);

        // Set activity's title (new note)
        String title_string;
        title_string = getString(R.string.new_note_header_text);
        setTitle(title_string);
    }

    // Gets text from fields (title and text) and adds to the db
    public void btnAddNote(View view) {

        EditText text;

        // Get note title from 'noteTitle' element
        text = findViewById(R.id.noteTitle);
        String note_title = text.getText().toString();

        // Get note text from 'noteText' element
        text = findViewById(R.id.noteText);
        String note_text = text.getText().toString();

        // Save data
        TXDatabase database = new TXDatabase(this);
        database.addNote(note_title, note_text, 0, 0);

        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
