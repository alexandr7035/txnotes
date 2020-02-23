package com.example.txnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.txnotes.db.TXDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Adds a new note and shows NewNoteActivity
    public void createNewNote(View view) {
        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        startActivity(intent);
    }


}
