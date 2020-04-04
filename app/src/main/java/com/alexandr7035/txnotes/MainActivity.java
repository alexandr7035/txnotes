package com.alexandr7035.txnotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.db.NotesDao;
import com.alexandr7035.txnotes.db.NotesDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements NotesRecycleViewAdapter.NoteClickListener,
                                     NotesRecycleViewAdapter.NoteLongClickListener {

    private TXNotesApplication app;

    // Recycleviw for list of notes
    public static RecyclerView recyclerView;
    public static NotesRecycleViewAdapter adapter;
    private List<NoteEntity> items;

    // Database
    private NotesDatabase db;
    private NotesDao db_dao;

    // Views
    private ConstraintLayout mainLayout;
    private TextView app_title;
    private FloatingActionButton delete_note_btn;
    private Snackbar snackbar;

    private Vibrator vibrator;

    private String LOG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.app = (TXNotesApplication) this.getApplication();

        mainLayout = findViewById(R.id.mainLayout);

        // DB
        db = app.getDatabaseInstance();
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

        // Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Set log tag
        this.LOG_TAG = app.getLogTag();

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

            int deleting_notes_count = adapter.getSelectedItems().size();

            // Snackbar to show deleted notes count and 'undo' button
            String text = getString(R.string.delete_notes_snack,
                            "" + deleting_notes_count);

            snackbar = Snackbar.make(
                    mainLayout,
                    text,
                    Snackbar.LENGTH_LONG
            );

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                            // Delete notes
                            for (NoteEntity note: adapter.getSelectedItems()) {
                                // Remove from adapter
                                items.remove(note);
                                // Remove from db
                                db_dao.deleteNote(note);
                                adapter.notifyDataSetChanged();

                                // Clear list of selected items
                                adapter.unselectAllItems();

                                // Hide the button
                                delete_note_btn.hide();

                                // Update title (notes count changed)
                                app_title.setText(getActivityTitleText());

                                // Vibrate
                                vibrator.vibrate(100);

                                // Show snackbar
                                snackbar.show();
                            }


                            // "No" button clicked
                        case DialogInterface.BUTTON_NEGATIVE:

                            // Clear list of selected items
                            adapter.unselectAllItems();
                            // Hide the button
                            delete_note_btn.hide();

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(Html.fromHtml(getString(R.string.dialog_delete_note, "" + deleting_notes_count)));
            builder.setPositiveButton(getString(R.string.dialog_positive), dialogClickListener);
            builder.setNegativeButton(getString(R.string.dialog_negative), dialogClickListener);

            AlertDialog dialog = builder.create();

            dialog.setCanceledOnTouchOutside(false);

            dialog.show();

            TextView msgTxt = dialog.findViewById(android.R.id.message);
            Button posBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dialog_message_text));
            posBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dialog_message_btn));
            negBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dialog_message_btn));
        }

    }

    public void menuBtnClicked(View v) {
        
    }
}