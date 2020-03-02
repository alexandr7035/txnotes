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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        // DB
        NotesDatabase db = ((TXNotesApplication) this.getApplication()).getDatabaseInstance();
        NotesDao db_dao = db.getNotesDao();

        // Get note_id
        Intent intent = getIntent();
        Integer note_id = intent.getIntExtra("clicked_note_id", 0);

        // FIXME - add modification date later !!!
        TextView dates_view = findViewById(R.id.noteDatesView);
        Long creation_date = db_dao.getNoteCreationDate(note_id);

        String dates_string = "<b> Создана: </b>" + DateFormat.format("dd-MM-yyyy  HH:mm", creation_date*1000).toString();
        dates_string += "<br><b> Изменена: </b>" + DateFormat.format("dd-MM-yyyy  HH:mm", creation_date*1000).toString();
        dates_view.setText(Html.fromHtml(dates_string));



        // Get note text by id
        String note_text = db_dao.getNoteText(note_id);

        // Get note's TextView object and set text
        TextView note_view = findViewById(R.id.showNoteView);
        note_view.setText(note_text);


    }



    public void cancelShowNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
