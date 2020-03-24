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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements NotesRecycleViewAdapter.NoteClickListener,
                                     NotesRecycleViewAdapter.NoteLongClickListener {


    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    List<NotesEntity> items;

    NotesDatabase db;
    NotesDao db_dao;

    TextView app_title;

    FloatingActionButton delete_note_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB
        db = ((TXNotesApplication) this.getApplication()).getDatabaseInstance();
        db_dao = db.getNotesDao();

        // Set activity's title, see setTitle method
        app_title = findViewById(R.id.appTitleView);
        app_title.setText(getActivityTitleText());


        items = db_dao.getAllNotes();
        recyclerView = (RecyclerView) findViewById(R.id.notesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesRecycleViewAdapter(items, this, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //
        delete_note_btn = findViewById(R.id.deleteNoteButton);
        delete_note_btn.hide();
    }


    @Override
    public void onNoteClick(int note_id, int position) {
        Intent intent = new Intent(this, ShowNoteActivity.class);
        Log.d("DEBUG_DB", "MainActivity: passed to show note: " + note_id);
        intent.putExtra("clicked_note_id", note_id);
        startActivity(intent);
    }

    @Override
    public void onLongNoteClick(int note_id, int position) {


        delete_note_btn.show();

        delete_note_btn.setOnClickListener(new DeleteBtnClickListener(note_id, position));

        // Update title (notes count changed)
        app_title.setText(getActivityTitleText());

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

    // Generate activity's title
    // (depending on notes count)
    public String getActivityTitleText() {
        Integer notes_count = db_dao.getNotesCount();
        return getString(R.string.app_title, " (" + notes_count + ")");
    }

    public class DeleteBtnClickListener implements View.OnClickListener {

        int deleting_note_id;
        int position;

        public DeleteBtnClickListener(int note_id, int position) {
            this.deleting_note_id = note_id;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            NotesEntity note = db_dao.getNoteById(deleting_note_id);
            db_dao.deleteNote(note);
            items.remove(position);
            adapter.notifyItemRemoved(position);
        }

    }
}