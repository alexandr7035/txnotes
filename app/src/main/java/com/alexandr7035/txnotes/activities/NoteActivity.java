package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.alexandr7035.txnotes.R;

public class NoteActivity extends AppCompatActivity
                          implements Toolbar.OnMenuItemClickListener {

    private final String LOG_TAG = "DEBUG_TXNOTES";

    private EditText noteTextView;
    private MutableLiveData<String> activityStateLiveData;

    private TextView toolbarTitle;

    private Menu menu;

    private int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

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
        invalidateOptionsMenu();

        // Init state LiveData
        activityStateLiveData = new MutableLiveData<String>();


        activityStateLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String activityState) {

                if (activityState != null) {
                    if (activityState.equals("STATE_CREATING")) {
                        toolbarTitle.setText(getString(R.string.activity_create_note_title));
                        noteTextView.setEnabled(true);
                    }

                    else if (activityState.equals("STATE_SHOWING")) {
                        toolbarTitle.setText(getString(R.string.activity_show_note_title));
                        noteTextView.setEnabled(false);

                    }

                    else if (activityState.equals("STATE_EDITING")) {
                        toolbarTitle.setText(getString(R.string.activity_edit_note_title));
                        noteTextView.setEnabled(true);
                    }

                }

            }
        });


        // Get note_id
        // 0 means a new note is creating
        Intent intent = getIntent();
        note_id = intent.getIntExtra("passed_note_id", 0);

        //
        if (note_id == 0) {
            activityStateLiveData.postValue("STATE_CREATING");
        }
        else {
            activityStateLiveData.postValue("STATE_SHOWING");
        }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {


        // FIXME
        // Simplify later
        if (item.getItemId() == R.id.toolbar_btn_change_activity_state) {

            Log.d(LOG_TAG, "btn clicked");

            if (activityStateLiveData.getValue() != null) {
                Log.d(LOG_TAG, "state not null");
                if (activityStateLiveData.getValue().equals("STATE_CREATING")) {
                    activityStateLiveData.postValue("STATE_SHOWING");
                }

                else if (activityStateLiveData.getValue().equals("STATE_SHOWING")) {
                    activityStateLiveData.postValue("STATE_EDITING");
                }

                else if (activityStateLiveData.getValue().equals("STATE_EDITING")) {
                    activityStateLiveData.postValue("STATE_SHOWING");
                }

            }
        }

        return super.onOptionsItemSelected(item);

    }


}




