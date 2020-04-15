package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alexandr7035.txnotes.R;

public class RecycleBinActivity extends AppCompatActivity {

    // Views
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);

        // Set activity's title, see setTitle method
        title = findViewById(R.id.activityTitle);
        title.setText(getActivityTitleText());

    }


    public void cancelRecycleBin(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Generate activity's title
    // (depending on notes count in recycle bin)
    public String getActivityTitleText() {
        int deleted_notes_count = 0;
        return getString(R.string.activity_recycle_bin_title, " (" + deleted_notes_count + ")");
    }

}
