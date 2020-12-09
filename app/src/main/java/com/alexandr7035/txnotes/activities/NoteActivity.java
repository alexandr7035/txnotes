package com.alexandr7035.txnotes.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alexandr7035.txnotes.R;
import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.dialogs.ExitConfirmationDialog;
import com.alexandr7035.txnotes.viewmodel.NoteViewModel;
import com.alexandr7035.txnotes.viewmodel.NoteViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.ExecutionException;

public class NoteActivity extends AppCompatActivity
                          implements Toolbar.OnMenuItemClickListener, ExitConfirmationDialog.ExitConfirmationDialogClickListener {

    private final String LOG_TAG = "DEBUG_TXNOTES";

    private EditText noteTextView;
    private MutableLiveData<String> activityStateLiveData;

    private TextView toolbarTitle;

    private MutableLiveData<NoteEntity> noteLiveData;

    private NoteViewModel viewModel;

    private BottomSheetDialog infoDialog;

    private Vibrator vibrator;

    private ExitConfirmationDialog exitConfirmationDialog;

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

        // Exit confirmation dialog
        // Shown if note is not saved
        exitConfirmationDialog = new ExitConfirmationDialog(this);
        exitConfirmationDialog.setOnButtonClickListener(this);

        // Init vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
                        toolbar.getMenu().findItem(R.id.item_copy_text).setVisible(false);

                    }

                    else if (activityState.equals("STATE_SHOWING")) {
                        toolbarTitle.setText(getString(R.string.activity_show_note_title));
                        noteTextView.setEnabled(false);

                        try {
                            noteLiveData.postValue(viewModel.getNote(note_id));
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_show_info).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_copy_text).setVisible(true);

                    }

                    else if (activityState.equals("STATE_EDITING")) {
                        toolbarTitle.setText(getString(R.string.activity_edit_note_title));
                        noteTextView.setEnabled(true);

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_show_info).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_copy_text).setVisible(false);

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

                // Hide modification date if same as creation date
                // (that means note wasn't modified)
                if (note.getNoteModificationDate() != note.getNoteCreationDate()) {
                    modificationDateView.setText(DateFormat.format("dd.MM.yyyy HH:mm", note.getNoteModificationDate() * 1000).toString());
                }
                else {
                    modificationDateView.setText("-");
                }
            }
        });


        // Show confirmation dialog if exit activity in EDITING or CREATING state
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "back arrow pressed");

                if (activityStateLiveData.getValue() != null) {
                    if (activityStateLiveData.getValue().equals("STATE_EDITING")) {
                        exitConfirmationDialog.show();
                        return;
                    }
                    else if (activityStateLiveData.getValue().equals("STATE_CREATING")) {
                        if (! noteTextView.getText().toString().equals("")) {
                            exitConfirmationDialog.show();
                            return;
                        }

                    }
                }

                finish();
            }
        });

    }


    // Show confirmation dialog if exit activity in EDITING or CREATING state
    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "back in navigation pressed");

        if (activityStateLiveData.getValue() != null) {
            if (activityStateLiveData.getValue().equals("STATE_EDITING")) {
                exitConfirmationDialog.show();
                return;
            }
            else if (activityStateLiveData.getValue().equals("STATE_CREATING")) {
                if (! noteTextView.getText().toString().equals("")) {
                    exitConfirmationDialog.show();
                    return;
                }

            }
        }

        super.onBackPressed();
    }
    

    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.item_save_note:


                // Prohibit saving empty notes
                if (noteTextView.getText().toString().trim().equals("")) {
                    vibrator.vibrate(300);

                    Toast toast = Toast.makeText(this, getString(R.string.toast_cant_save_empty_note),
                                Toast.LENGTH_SHORT);
                    toast.show();
                    break;

                }

                saveNote();

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

            case R.id.item_copy_text:
                // Copy note text to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("note_text", noteTextView.getText().toString());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast toast = Toast.makeText(this, getString(R.string.toast_text_copied),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
        }

        return super.onOptionsItemSelected(item);

    }


    // Handle exit confirmation dialog
    @Override
    public void exitConfirmationDialogPositiveClick() {

        String ACTIVITY_STATE = activityStateLiveData.getValue();

        if (ACTIVITY_STATE != null) {
            if (ACTIVITY_STATE.equals("STATE_EDITING")) {
                activityStateLiveData.postValue("STATE_SHOWING");
                exitConfirmationDialog.dismiss();
            }
            else if (ACTIVITY_STATE.equals("STATE_CREATING")) {
                finish();
            }
        }

    }

    @Override
    public void exitConfirmationDialogNegativeClick() {
        exitConfirmationDialog.dismiss();
    }


    // Saves note or creates a new one 
    private void saveNote() {
        // Create a new note
        // Learn id of created note in order to correctly update state livedata
        if (note_id == 0) {

            NoteEntity note = new NoteEntity();

            // Set modification date same as creation date
            // In order to implement correct sorting by date
            // If 2 dates are equal, just hide modification date on a view level
            long creation_date = System.currentTimeMillis() / 1000;
            note.setNoteCreationDate(creation_date);
            note.setNoteModificationDate(creation_date);

            note.setNoteText(noteTextView.getText().toString());

            try {
                note_id = viewModel.createNote(note);
                Log.d(LOG_TAG, "CREATED NOTE ID " + note_id);
            } catch (ExecutionException | InterruptedException e) {
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

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }


        // Update LiveData
        try {
            NoteEntity note = viewModel.getNote(note_id);
            noteLiveData.postValue(note);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}




