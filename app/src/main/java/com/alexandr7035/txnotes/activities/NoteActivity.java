package com.alexandr7035.txnotes.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.text.method.KeyListener;
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

    private final int MAX_TITLE_LENGTH = 30;
    private final String LOG_TAG = "DEBUG_TXNOTES";

    private NoteViewModel viewModel;
    private MutableLiveData<String> activityStateLiveData;
    private MutableLiveData<NoteEntity> noteLiveData;

    private TextView toolbarTitle;
    private EditText noteTitleView;
    private EditText noteTextView;

    // Key listeners for editable fields
    private KeyListener defaultKeyListener;
    private final KeyListener showingKeyListener = null;

    private BottomSheetDialog infoDialog;
    private ExitConfirmationDialog exitConfirmationDialog;

    private Vibrator vibrator;

    private long note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //Log.d(LOG_TAG, "start NoteActivity");

        // ViewModel
        viewModel = new ViewModelProvider(this, new NoteViewModelFactory(this.getApplication())).get(NoteViewModel.class);


        // Views
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        noteTextView = findViewById(R.id.noteTextView);
        noteTitleView = findViewById(R.id.noteTitleView);

        // Save key listener
        // Key listeners are equal for all edit texts, so get 1
        defaultKeyListener = noteTextView.getKeyListener();

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

                        noteTextView.setTextIsSelectable(true);
                        noteTextView.setKeyListener(defaultKeyListener);

                        noteTitleView.setTextIsSelectable(true);
                        noteTitleView.setKeyListener(defaultKeyListener);

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_show_info).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_copy_text).setVisible(false);

                    }

                    else if (activityState.equals("STATE_SHOWING")) {
                        toolbarTitle.setText(getString(R.string.activity_show_note_title));

                        noteTextView.setTextIsSelectable(true);
                        noteTextView.setKeyListener(null);

                        noteTitleView.setTextIsSelectable(true);
                        noteTitleView.setKeyListener(null);

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

                        noteTextView.setTextIsSelectable(true);
                        noteTextView.setKeyListener(defaultKeyListener);

                        noteTitleView.setTextIsSelectable(true);
                        noteTitleView.setKeyListener(defaultKeyListener);

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
                noteTitleView.setText(note.getNoteTitle());

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
                //Log.d(LOG_TAG, "back arrow pressed");

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
        //Log.d(LOG_TAG, "back in navigation pressed");

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

        int itemId = item.getItemId();
        
        if (itemId == R.id.item_save_note) {// Prohibit saving empty notes
            if (noteTextView.getText().toString().trim().equals("") && noteTitleView.getText().toString().trim().equals("")) {
                vibrator.vibrate(200);

                Toast toast = Toast.makeText(this, getString(R.string.toast_cant_save_empty_note),
                        Toast.LENGTH_SHORT);
                toast.show();
                return super.onOptionsItemSelected(item);

            }

            saveNote();

            if (activityStateLiveData.getValue() != null) {
                activityStateLiveData.postValue("STATE_SHOWING");
            }

        }

        else if (itemId == R.id.item_edit_note) {
            if (activityStateLiveData.getValue() != null) {
                activityStateLiveData.postValue("STATE_EDITING");
            }
        }

        else if (itemId == R.id.item_show_info) {// Show info dialog
            // The info is updating inside noteLiveData observer
            infoDialog.show();
        }

        else if (itemId == R.id.item_copy_text) {// Copy note text to clipboard
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
        // If note_id is 0, create a new note
        // Else get existing from viewmodel and update it

        NoteEntity note;

        if (note_id == 0) {
            note = new NoteEntity();

            // Set modification date same as creation date
            // In order to implement correct sorting by date
            // If 2 dates are equal, just hide modification date on a view level
            long creation_date = System.currentTimeMillis() / 1000;
            note.setNoteCreationDate(creation_date);
            note.setNoteModificationDate(creation_date);
        }
        else {
            try {
                // Get existing note
                note = viewModel.getNote(note_id);
                note.setNoteModificationDate(System.currentTimeMillis() / 1000);
            }
            catch (InterruptedException | ExecutionException exception) {
                Toast.makeText(this, getString(R.string.toast_saving_error), Toast.LENGTH_LONG).show();
                return ;
            }
        }


        note.setNoteText(noteTextView.getText().toString());

        String note_title_text = noteTitleView.getText().toString().trim();
        if (note_title_text.equals("")) {
            // If title is not specified get first 30 symbols from note text
            note.setNoteTitle(getSubstring(noteTextView.getText().toString().trim().replaceAll("[\\t\\n\\r]+"," "), 30));
        }
        else {
            note.setNoteTitle(noteTitleView.getText().toString().trim());
        }


        // Save new note
        if (note_id == 0) {
            try {
                // Update note_id for next uses
                note_id = viewModel.createNote(note);
                //Log.d(LOG_TAG, "CREATED NOTE ID " + note_id);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, getString(R.string.toast_saving_error), Toast.LENGTH_LONG).show();
                return ;
            }
        }
        // Update existing note
        else {
            viewModel.updateNote(note);
        }

        noteLiveData.postValue(note);

    }


    private String getSubstring(String s, int symbols) {
        return s.substring(0, Math.min(s.length(), MAX_TITLE_LENGTH)).trim();
    }
}




