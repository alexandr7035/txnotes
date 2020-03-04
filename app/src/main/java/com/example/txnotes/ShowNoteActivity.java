package com.example.txnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.example.txnotes.db.NotesDao;
import com.example.txnotes.db.NotesDatabase;

public class ShowNoteActivity extends AppCompatActivity {

    Integer note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        // DB
        NotesDatabase db = ((TXNotesApplication) this.getApplication()).getDatabaseInstance();
        NotesDao db_dao = db.getNotesDao();

        // Get note_id
        Intent intent = getIntent();
        this.note_id = intent.getIntExtra("clicked_note_id", 0);

        TextView dates_view = findViewById(R.id.noteDatesView);
        Long creation_date = db_dao.getNoteCreationDate(this.note_id);
        Long modification_date = db_dao.getNoteModificationDate(this.note_id);

        // Creation date string
        String creation_date_string = "<b> Создана: </b>" + DateFormat.format("dd-MM-yyyy  HH:mm", creation_date*1000).toString();

        // Modification date string
        String modification_date_string;
        modification_date_string = "<br><b> Изменена: </b>";
        if (modification_date == 0) {
            modification_date_string += "-";
        }
        else {
            modification_date_string += DateFormat.format("dd-MM-yyyy  HH:mm", modification_date * 1000).toString();
        }

        String dates_string = creation_date_string + modification_date_string;
        dates_view.setText(Html.fromHtml(dates_string));

        // Get note text by id
        String note_text = db_dao.getNoteText(this.note_id);

        // Get note's TextView object and set text
        TextView note_view = findViewById(R.id.showNoteView);
        note_view.setText(note_text);


    }

    public void cancelShowNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnEditNote(View view) {
        Intent intent = new Intent(this, EditNoteActivity.class);            // Go to CreateNewNoteActivity);
        intent.putExtra("edited_note_id", this.note_id);
        startActivity(intent);


    }

}
