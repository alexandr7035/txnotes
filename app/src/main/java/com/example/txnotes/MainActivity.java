package com.example.txnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;
import com.example.txnotes.db.NotesEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesRecycleViewAdapter.NoteClickListener {


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
        adapter= new NotesRecycleViewAdapter(items, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onNoteClick(int note_id) {
        Intent intent = new Intent(this, ShowNoteActivity.class);
        Log.d("DEBUG_DB", "MainActivity: passed to show note: " + note_id);
        intent.putExtra("clicked_note_id", note_id);
        startActivity(intent);
    }


    // Adds a new note and shows NewNoteActivity
    public void createNewNote(View view) {
        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        startActivity(intent);
    }

    // Override back button in MainActivity
    @Override
    public void onBackPressed() {
        // Minimize the app
        this.moveTaskToBack(true);
    }

}