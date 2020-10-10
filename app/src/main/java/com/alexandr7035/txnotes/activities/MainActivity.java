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
import com.alexandr7035.txnotes.adapters.NotesRecyclerViewAdapter;
import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.viewmodel.MainViewModel;
import com.alexandr7035.txnotes.viewmodel.MainViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    // Recycleviw for list of notes
    public static RecyclerView recyclerView;
    public static NotesRecyclerViewAdapter adapter;

    private DefaultClickListener defaultClickListener;
    private SelectionClickListener selectionClickListener;


    // Views
    private ConstraintLayout mainLayout;
    private TextView toolbarTitle;
    private FloatingActionButton deleteNoteBtn;
    private FloatingActionButton createNoteBtn;
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
        createNoteBtn = findViewById(R.id.createNoteButton);

        // A button to delete note (hidden by default, shown when note is selected)
        deleteNoteBtn = findViewById(R.id.deleteNoteButton);
        deleteNoteBtn.hide();

        // Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main_activity_toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);

        // Init recyclerview
        recyclerView = findViewById(R.id.notesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Click listeners for recyclerview items
        selectionClickListener = new SelectionClickListener();
        defaultClickListener = new DefaultClickListener();

        adapter = new NotesRecyclerViewAdapter();

        // Set default click listeners
        adapter.setItemClickListener(defaultClickListener);
        adapter.setItemLongClickListener(defaultClickListener);

        recyclerView.setAdapter(adapter);



        // Viewmodel & livedata
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this.getApplication())).get(MainViewModel.class);

        notesListLiveData = viewModel.getNotesList();
        notesCountLiveData = viewModel.getNotesCount();
        selectedNotesLiveData = viewModel.getSelectedNotesLiveData();

        selectedNotes = new ArrayList<>();

                // Watch for notes list and update the UI
        notesListLiveData.observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> notes) {
                 // Update items in adapter
                 if (notes != null) {
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
            public void onChanged(@Nullable List<NoteEntity> selectedNotes) {
               // Log.d(LOG_TAG, "selected notes livedata state" + selectedNotes.toString());

                if (selectedNotes != null) {

                    if (! selectedNotes.isEmpty()) {
                        Log.d(LOG_TAG, "some items are selected: " + selectedNotes);

                        createNoteBtn.hide();
                        deleteNoteBtn.show();

                        Log.d(LOG_TAG, "set selection click listener");


                        adapter.setItemClickListener(selectionClickListener);
                        adapter.setItemLongClickListener(selectionClickListener);


                    } else {
                        Log.d(LOG_TAG, "no items selected now");

                        deleteNoteBtn.hide();
                        createNoteBtn.show();

                        Log.d(LOG_TAG, " set default click listener");
                        adapter.setItemClickListener(defaultClickListener);
                        adapter.setItemLongClickListener(defaultClickListener);

                    }
                }

            }
        });




    }




    // Default click listener for recyclerview items
    class DefaultClickListener implements NotesRecyclerViewAdapter.NoteClickListener,
           NotesRecyclerViewAdapter.NoteLongClickListener {


        @Override
        public void onNoteClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "clicked position " + position + " id " + skill_id);

            Log.d(LOG_TAG, "called defult onCLick");
            Log.d(LOG_TAG, "start new activity (SHOW)");

            Intent intent = new Intent(MainActivity.this, ShowNoteActivity.class);
            intent.putExtra("clicked_note_id", skill_id);
            startActivity(intent);
        }

        @Override
        public void onLongNoteClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "clicked (LONG) position " + position + " id " + skill_id);

            adapter.selectItem(position);
            selectedNotesLiveData.postValue(adapter.getSelectedItems());

        }
    }


    // Set if at least one item in RecyclerView is selected
    // Replaced by default click listener when no items selected
    class SelectionClickListener implements NotesRecyclerViewAdapter.NoteClickListener,
            NotesRecyclerViewAdapter.NoteLongClickListener {


        @Override
        public void onNoteClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "SELECTED_CL: click item " + position + " skill_id " + skill_id);

            // Select item if not selected
            // Else unselect

            Log.d(LOG_TAG, "ON_CLICK SELECTION CALLED");

            if (adapter.checkIfItemSelected(position)) {
                adapter.unselectItem(position);
            }
            else {
               adapter.selectItem(position);
            }

            // Update livedata
           selectedNotesLiveData.postValue(adapter.getSelectedItems());

        }

        @Override
        public void onLongNoteClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "SELECTED_CL: LONG click item " + position + " skill_id " + skill_id);

            // Do nothing
            // May be changed later
        }
    }



    // DeleteNoteButton
    public void deleteNoteBtnAction(View v) {

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
                                // Remove from db
                                viewModel.removeNote(note);
                            }

                            adapter.unselectAllItems();
                            selectedNotesLiveData.setValue(adapter.getSelectedItems());

                            vibrator.vibrate(100);
                            snackbar.show();
                            
                        // "No" button clicked
                        case DialogInterface.BUTTON_NEGATIVE:


                            adapter.unselectAllItems();
                            selectedNotesLiveData.setValue(adapter.getSelectedItems());


                    }

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
            deleteNoteBtn.hide();
            createNoteBtn.show();
        }
    }


    // Shows NewNoteActivity
    public void createNewNoteAction(View view) {
        // Go to CreateNewNoteActivity
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        startActivity(intent);
    }


}