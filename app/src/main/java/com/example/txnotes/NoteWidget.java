package com.example.txnotes;

import android.content.Context;
import android.text.Html;
import android.text.format.DateFormat;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

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

    public void setNoteDate(Long date) { ;
        TextView textview = findViewWithTag("noteDateView");
        
        String string_date = "<b>" + DateFormat.format("dd-MM-yyyy", date*1000).toString()
                + "</b><br>" + DateFormat.format("HH:mm", date*1000).toString();

        textview.setText(Html.fromHtml(string_date));
    }
}
