package com.alexandr7035.txnotes;

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

import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.db.NotesDao;
import com.alexandr7035.txnotes.db.NotesDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements NotesRecycleViewAdapter.NoteClickListener,
                                     NotesRecycleViewAdapter.NoteLongClickListener {


    // Recycleviw for list of notes
    public static RecyclerView recyclerView;
    public static NotesRecycleViewAdapter adapter;
    private List<NoteEntity> items;

    // Database
    private NotesDatabase db;
    private NotesDao db_dao;

    // Views
    private TextView app_title;
    private FloatingActionButton delete_note_btn;

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

        // Hide button if visible
        if (delete_note_btn.getVisibility() == View.VISIBLE && adapter.getSelectedItems().size() == 0) {
            delete_note_btn.hide();
            return;
        }

        if ( ! adapter.checkIfAnyItemSelected()) {
            Intent intent = new Intent(this, ShowNoteActivity.class);
            Log.d("DEBUG_DB", "MainActivity: passed to show note: " + note_id);
            intent.putExtra("clicked_note_id", note_id);
            startActivity(intent);
        }
    }

    @Override
    public void onLongNoteClick(int note_id, int position) {

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
    // Minimize the app if no items selected
    // Else clear selection
    @Override
    public void onBackPressed() {

        if ( ! adapter.checkIfAnyItemSelected()) {
            // Minimize the app
            this.moveTaskToBack(true);
        }
        else {
            adapter.unselectAllItems();
            delete_note_btn.hide();
        }
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
            vibrator.vibrate(100);

            // Delete notes
            for (NoteEntity note: adapter.getSelectedItems()) {
                // Remove from adapter
                items.remove(note);
                // Remove from db
                db_dao.deleteNote(note);
                adapter.notifyDataSetChanged();
            }

            // Clear list of selected items
            adapter.unselectAllItems();

            // Hide the button
            delete_note_btn.hide();

            // Update title (notes count changed)
            app_title.setText(getActivityTitleText());
        }

    }
}