package com.example.txnotes;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteWidget extends LinearLayout {

    public NoteWidget(Context context) {
        super(context);



        //this = View.inflate(getContext(), R.layout.view_note, null);
        this.inflate(context, R.layout.view_note, this);
    }

    public void setNoteTitle(String title) {
        TextView textview = findViewWithTag("noteTitleView");
        textview.setText(title);
    }
}
