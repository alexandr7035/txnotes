package com.example.txnotes;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteWidget extends LinearLayout {

    public NoteWidget(Context context) {
        super(context);

        // Apply view_note.xml
        this.inflate(context, R.layout.view_note, this);
    }

    public void setNoteTitle(String title) {
        TextView textview = findViewWithTag("noteTitleView");
        textview.setText(title);
    }

    public void setNoteDate(Long date) {
        TextView textview = findViewWithTag("noteDateView");
        textview.setText("" + date);
    }
}
