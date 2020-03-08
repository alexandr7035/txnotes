package com.example.txnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;
import com.example.txnotes.db.NotesEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    List<NotesEntity> items;

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



        items = db_dao.getAllNotes();
        recyclerView= (RecyclerView)findViewById(R.id.notesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new NotesRecycleViewAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
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

            // Go to CreateNewNoteActivity
            Intent intent = new Intent(v.getContext(), ShowNoteActivity.class);
            intent.putExtra("clicked_note_id", this.note_id);
            startActivity(intent);
        }

    }


    // Override back button in MainActivity
    @Override
    public void onBackPressed() {
        // Minimize the app
        this.moveTaskToBack(true);
    }

}