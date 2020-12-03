package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
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

public class NoteActivity extends AppCompatActivity
                          implements Toolbar.OnMenuItemClickListener {

    private final String LOG_TAG = "DEBUG_TXNOTES";

    private EditText noteTextView;
    private MutableLiveData<String> activityStateLiveData;

    private TextView toolbarTitle;

    private MutableLiveData<NoteEntity> noteLiveData;

    private NoteViewModel viewModel;

    private int note_id;

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

        // Init state LiveData
        activityStateLiveData = new MutableLiveData<String>();


        // Get note_id
        // 0 means a new note is creating
        Intent intent = getIntent();
        note_id = intent.getIntExtra("passed_note_id", 0);

        // Init note object
        // Set initial activity state
        if (note_id == 0) {
            noteLiveData = new MutableLiveData<>();

            NoteEntity note = new NoteEntity();
            note.setNoteText("123");
            noteLiveData.postValue(note);

            activityStateLiveData.postValue("STATE_CREATING");
        }

        else {
            noteLiveData = viewModel.getNoteLiveData(note_id);
            activityStateLiveData.postValue("STATE_SHOWING");
        }





        activityStateLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String activityState) {

                if (activityState != null) {
                    if (activityState.equals("STATE_CREATING")) {
                        toolbarTitle.setText(getString(R.string.activity_create_note_title));
                        noteTextView.setEnabled(true);

                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(true);

                    }

                    else if (activityState.equals("STATE_SHOWING")) {
                        toolbarTitle.setText(getString(R.string.activity_show_note_title));
                        noteTextView.setEnabled(false);

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(true);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(false);

                    }

                    else if (activityState.equals("STATE_EDITING")) {
                        toolbarTitle.setText(getString(R.string.activity_edit_note_title));
                        noteTextView.setEnabled(true);

                        toolbar.getMenu().findItem(R.id.item_edit_note).setVisible(false);
                        toolbar.getMenu().findItem(R.id.item_save_note).setVisible(true);

                    }

                }

            }
        });


        noteLiveData.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity note) {
                noteTextView.setText(note.getNoteText());

            }
        });


    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        
        if (item.getItemId() == R.id.item_save_note) {

            if (activityStateLiveData.getValue() != null) {
                    activityStateLiveData.postValue("STATE_SHOWING");
            }

        }

        else if (item.getItemId() == R.id.item_edit_note) {

            if (activityStateLiveData.getValue() != null) {
                activityStateLiveData.postValue("STATE_EDITING");
            }

        }

        return super.onOptionsItemSelected(item);

    }


}




