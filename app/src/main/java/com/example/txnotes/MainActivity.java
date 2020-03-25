package com.example.txnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.txnotes.db.NoteEntity;
import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements NotesRecycleViewAdapter.NoteClickListener,
                                     NotesRecycleViewAdapter.NoteLongClickListener {


    // Recycleviw for list of notes
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    private List<NoteEntity> items;

    // Database
    private NotesDatabase db;
    private NotesDao db_dao;

    // Views
    private TextView app_title;
    private FloatingActionButton delete_note_btn;

    // Lists for notes being deleted
    private ArrayList<Integer> notesToDeleteIdsList = new ArrayList<Integer>();
    private ArrayList<Integer> notesToDeletePositionsList = new ArrayList<Integer>();

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

        // Recycleview settings
        recyclerView = (RecyclerView) findViewById(R.id.notesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Notes data from database
        items = db_dao.getAllNotes();
        // Pass "this" as click listener (MainActivity implements click listeners from NotesRecycleViewAdapter)
        adapter = new NotesRecycleViewAdapter(items, this, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        // A button to delete note (hidden by default, shown when note is selected)
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

        // Add to notesToDeleteList
        notesToDeleteIdsList.add(note_id);
        notesToDeletePositionsList.add(position);
        Log.d("DEBUG_TXNOTES", "Deleted ids: " + notesToDeleteIdsList.toString());
        Log.d("DEBUG_TXNOTES", "Deleted positions: " + notesToDeletePositionsList.toString());

        delete_note_btn.show();

        delete_note_btn.setOnClickListener(new DeleteBtnClickListener());

    }


    // Shows NewNoteActivity
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
        int notes_count = db_dao.getNotesCount();
        return getString(R.string.app_title, " (" + notes_count + ")");
    }


    // DeleteNoteButton is shown where any note is selected by long click
    // Deletes note
    public class DeleteBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Vibrate when deleting
            Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
           //vibrator.vibrate(200);

            // Delete from db
            for (int id : MainActivity.this.notesToDeleteIdsList) {
                Log.d("DEBUG_TXNOTES", "Deleted id: " + id);
                NoteEntity note = db_dao.getNoteById(id);
                db_dao.deleteNote(note);
            }

            // Refresh adapter
            for (int position: MainActivity.this.notesToDeletePositionsList) {
                Log.d("DEBUG_TXNOTES", "Deleted position: " + position);
                items.remove(position);
                adapter.notifyItemRemoved(position);
            }

            // Clear lists
            MainActivity.this.notesToDeleteIdsList.clear();
            MainActivity.this.notesToDeletePositionsList.clear();

            // Hide the button
            delete_note_btn.hide();

            // Update title (notes count changed)
            app_title.setText(getActivityTitleText());
        }

    }
}