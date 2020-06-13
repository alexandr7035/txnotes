package com.alexandr7035.txnotes.activities;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandr7035.txnotes.R;
import com.alexandr7035.txnotes.adapters.NotesRecycleViewAdapter;
import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.viewmodel.MainViewModel;
import com.alexandr7035.txnotes.viewmodel.MainViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements NotesRecycleViewAdapter.NoteClickListener,
                                     NotesRecycleViewAdapter.NoteLongClickListener {


    // Recycleviw for list of notes
    public static RecyclerView recyclerView;
    public static NotesRecycleViewAdapter adapter;
    private List<NoteEntity> notes_list;

    // Views
    private ConstraintLayout mainLayout;
    private TextView toolbarTitle;
    private FloatingActionButton delete_note_btn;
    private FloatingActionButton createNoteButton;
    private Snackbar snackbar;
    private Toolbar toolbar;

    private Vibrator vibrator;

    private final String LOG_TAG = "DEBUG_TXNOTES";


    private LiveData<List<NoteEntity>> notesListLiveData;
    private LiveData<Integer> notesCountLiveData;
    private MainViewModel viewModel;

    private MutableLiveData<List<NoteEntity>> selectedNotesLiveData;
    private List<NoteEntity> selectedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);

        // A button to create note
        createNoteButton = findViewById(R.id.createNoteButton);

        // A button to delete note (hidden by default, shown when note is selected)
        delete_note_btn = findViewById(R.id.deleteNoteButton);
        delete_note_btn.hide();

        // Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main_activity_toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);

        // Init recyclerview
        recyclerView = findViewById(R.id.notesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesRecycleViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

        // Viewmodel & livedata
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this.getApplication())).get(MainViewModel.class);

        notesListLiveData = viewModel.getNotesList();
        notesCountLiveData = viewModel.getNotesCount();
        selectedNotesLiveData = viewModel.getSelectedNotes();

        selectedNotes = new ArrayList<>();

                // Watch for notes list and update the UI
        notesListLiveData.observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> notes) {
                 // Update items in adapter
                 if (notes != null && ! notes.isEmpty()) {
                        adapter.setItems(notes);
                 }
            }
        });


        // Watch for notes count and update the ui
        notesCountLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer notesCount) {
                // Change toolbar title
                toolbarTitle.setText(getString(R.string.activity_main_title, notesCount));
            }

        });

        // Watch fr notes selection and update the ui
        selectedNotesLiveData.observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> selectedNotes) {
                Log.d(LOG_TAG, "selected notes livedata state" + selectedNotes.toString());

            }
        });




    }


    @Override
    public void onNoteClick(int note_id, int position) {

        // Hide button if visible
        if (delete_note_btn.getVisibility() == View.VISIBLE && adapter.getSelectedItems().size() == 0) {
            delete_note_btn.hide();
            createNoteButton.show();
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

        createNoteButton.hide();
        delete_note_btn.show();
        delete_note_btn.setOnClickListener(new DeleteBtnClickListener());

        Log.d(LOG_TAG, "SET VALUES TO LIVEDATA selected notss");
        selectedNotes.add(new NoteEntity("1", 123));
        selectedNotesLiveData.setValue(selectedNotes);

    }


    // DeleteNoteButton is shown where any note is selected by long click
    // Deletes note
    public class DeleteBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int deleting_notes_count = adapter.getSelectedItems().size();

            // Snackbar to show deleted notes count and 'undo' button
            String text = getString(R.string.snack_delete_notes,
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
                                notes_list.remove(note);
                                // Remove from db
                                //db_dao.deleteNote(note);

                            }

                            adapter.notifyDataSetChanged();

                            // Hide the button and clear list of selected items
                            // Update activity's title (notes count changed)
                            // Also vibrate and show the snackbar
                            adapter.unselectAllItems();
                            delete_note_btn.hide();

                            vibrator.vibrate(100);
                            snackbar.show();
                            
                        // "No" button clicked
                        case DialogInterface.BUTTON_NEGATIVE:

                            // // Hide the button and clear list of selected items
                            adapter.unselectAllItems();
                            delete_note_btn.hide();

                    }

                    createNoteButton.show();
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(Html.fromHtml(getString(R.string.dialog_delete_note_message, "" + deleting_notes_count)));
            builder.setPositiveButton(getString(R.string.dialog_delete_note_positive), dialogClickListener);
            builder.setNegativeButton(getString(R.string.dialog_delete_note_negative), dialogClickListener);

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
            createNoteButton.show();
        }
    }


    // Shows NewNoteActivity
    public void createNewNoteAction(View view) {
        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        startActivity(intent);
    }


}