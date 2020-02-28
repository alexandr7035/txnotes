package com.example.txnotes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import static com.example.txnotes.Other.dpToPixels;


public class NoteWidget extends LinearLayout {

    // Note height
    Integer NOTE_HEIGHT = dpToPixels(getContext(), 70);

    // Distance after the note ( ~ bottom margin)
    Integer DISTANCE_AFTER_NOTE = dpToPixels(getContext(), 10);

    public NoteWidget(Context context) {
        super(context);

        // Set color
        this.setBackgroundColor(getResources().getColor(R.color.noteWidget));

        // Set height
        this.setMinimumHeight(this.NOTE_HEIGHT);

        // Set layout's orientation
        this.setOrientation(LinearLayout.HORIZONTAL);

    }
}
