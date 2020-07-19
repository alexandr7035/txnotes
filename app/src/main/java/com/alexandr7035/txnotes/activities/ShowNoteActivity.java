package com.alexandr7035.txnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexandr7035.txnotes.R;
import com.alexandr7035.txnotes.db.NoteEntity;
import com.alexandr7035.txnotes.viewmodel.ShowNoteViewModel;
import com.alexandr7035.txnotes.viewmodel.ShowNoteViewModelFactory;

import java.util.concurrent.ExecutionException;

public class ShowNoteActivity extends AppCompatActivity {

    int note_id;
    private ShowNoteViewModel viewModel;

    private final String LOG_TAG = "DEBUG_TXNOTES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        // Get note_id
        Intent intent = getIntent();
        this.note_id = intent.getIntExtra("clicked_note_id", 0);

        // ViewModel
        viewModel = new ViewModelProvider(this, new ShowNoteViewModelFactory(this.getApplication())).get(ShowNoteViewModel.class);

        // Get note data
        NoteEntity note = null;
        try {
            note = viewModel.getNote(note_id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Get note's datas from the db and set to the view
        TextView dates_view = findViewById(R.id.noteDatesView);

        long creation_date = note.getNoteCreationDate();
        long modification_date = note.getNoteModificationDate();

        // Creation date string
        String creation_date_str = "<b> Создана: </b>" + DateFormat.format("dd-MM-yyyy  HH:mm", creation_date*1000).toString();

        // Modification date string
        String modification_date_str;
        modification_date_str = "<br><b> Изменена: </b>";
        if (modification_date == creation_date) {
            modification_date_str += "-";
        }
        else {
            modification_date_str += DateFormat.format("dd-MM-yyyy  HH:mm", modification_date * 1000).toString();
        }

        String dates_str = creation_date_str + modification_date_str;
        dates_view.setText(Html.fromHtml(dates_str));

        // Get note text by id
        String note_text = note.getNoteText();

        // Get note's TextView object and set text
        TextView note_view = findViewById(R.id.showNoteView);
        note_view.setText(note_text);


    }

    public void cancelShowNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnEditNote(View view) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("edited_note_id", this.note_id);
        startActivity(intent);


    }

}
