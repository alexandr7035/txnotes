package com.example.txnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
}
