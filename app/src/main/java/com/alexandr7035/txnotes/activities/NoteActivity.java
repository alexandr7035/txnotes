package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alexandr7035.txnotes.R;
import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.viewmodel.NoteViewModel;
import com.alexandr7035.txnotes.viewmodel.NoteViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.ExecutionException;

public class NoteActivity extends AppCompatActivity
                          implements Toolbar.OnMenuItemClickListener {

    private final String LOG_TAG = "DEBUG_TXNOTES";

    private EditText noteTextView;
    private MutableLiveData<String> activityStateLiveData;

    private TextView toolbarTitle;

    private MutableLiveData<NoteEntity> noteLiveData;

    private NoteViewModel viewModel;

    private BottomSheetDialog infoDialog;

    private long note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Log.d(LOG_TAG, "start NoteActivity");

        // ViewModel
        viewModel = new ViewModelProvider(this, new NoteViewModelFactory(this.getApplication())).get(NoteViewModel.class);


        // Views
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        noteTextView = findViewById(R.id.noteTextView);



        // Close activity on navigation btn click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.inflateMenu(R.menu.menu_note_activity_toolbar);
        toolbar.setOnMenuItemClickListener(this);

        // Init info dialog
        infoDialog = new BottomSheetDialog(this);
        infoDialog.setContentView(R.layout.dialog_note_info);


        // Init state LiveData
        activityStateLiveData = new MutableLiveData<String>();


        // Get note_id
        // 0 means a new note is creating
        Intent intent = getIntent();
        note_id = intent.getLongExtra("passed_note_id", 0);

        // Init note object
        // Set initial activity state
        noteLiveData = new MutableLiveData<>();
        if (note_id == 0) {

            activityStateLiveData.postValue("STATE_CREATING");
        }

        else {
            try {
                noteLiveData.postValue(viewModel.getNote(note_id));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activityStateLiveData.postValue("STATE_SHOWING");
        }





        activityStateLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String activityState) {

                if (activityState != null) {
                    if (activityState.equals("STATE_CREATING")) {
                        toolbarTitle.setText(getString(R.string.activity_create_note_title));
                        noteTextView.setEnabled(true);

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_show_info).setVisible(false);

                    }

                    else if (activityState.equals("STATE_SHOWING")) {
                        toolbarTitle.setText(getString(R.string.activity_show_note_title));
                        noteTextView.setEnabled(false);

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_show_info).setVisible(true);


                    }

                    else if (activityState.equals("STATE_EDITING")) {
                        toolbarTitle.setText(getString(R.string.activity_edit_note_title));
                        noteTextView.setEnabled(true);

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_show_info).setVisible(false);

                    }

                }

            }
        });


        noteLiveData.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity note) {

                noteTextView.setText(note.getNoteText());

                // Views
                TextView creationDateView = infoDialog.findViewById(R.id.note_info_creation_date_value);
                TextView modificationDateView = infoDialog.findViewById(R.id.note_info_modification_date_value);

                creationDateView.setText(DateFormat.format("dd.MM.yyyy HH:mm", note.getNoteCreationDate() * 1000).toString());

                if (note.getNoteModificationDate() != 0) {
                    modificationDateView.setText(DateFormat.format("dd.MM.yyyy HH:mm", note.getNoteModificationDate() * 1000).toString());
                }
                else {
                    modificationDateView.setText("-");
                }
            }
        });


    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.item_save_note:

                // Init note object


                // Create a new note
                // Learn id of created note in order to correctly update state livedata
                if (note_id == 0) {

                    NoteEntity note = new NoteEntity();
                    note.setNoteCreationDate(System.currentTimeMillis() / 1000);
                    note.setNoteText(noteTextView.getText().toString());

                    try {
                        note_id = viewModel.createNote(note);
                        Log.d(LOG_TAG, "CREATED NOTE ID " + note_id);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                else {
                    Log.d(LOG_TAG, "EDITED NOTE ID " + note_id);

                    try {
                        NoteEntity note = viewModel.getNote(note_id);
                        note.setNoteModificationDate(System.currentTimeMillis() / 1000);
                        note.setNoteText(noteTextView.getText().toString());
                        viewModel.updateNote(note);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


                // Update LiveData
                try {
                    NoteEntity note = viewModel.getNote(note_id);
                    noteLiveData.postValue(note);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (activityStateLiveData.getValue() != null) {
                    activityStateLiveData.postValue("STATE_SHOWING");
                }

                break;

            case R.id.item_edit_note:

                if (activityStateLiveData.getValue() != null) {
                    activityStateLiveData.postValue("STATE_EDITING");
                }

                break;

            case R.id.item_show_info:
                // Show info dialog
                // The info is updating inside noteLiveData observer
                infoDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }


}




