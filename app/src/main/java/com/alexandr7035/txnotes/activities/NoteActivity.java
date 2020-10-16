package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alexandr7035.txnotes.R;

public class NoteActivity extends AppCompatActivity {

    // Views
    private Toolbar toolbar;

    private int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        toolbar = findViewById(R.id.toolbar);
        // Close activity on navigation btn click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Get note_id
        Intent intent = getIntent();
        note_id = intent.getIntExtra("passed_note_id", 0);

        if (note_id == 0) {

        }

    }



}
