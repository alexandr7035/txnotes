package com.example.txnotes;

import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.txnotes.db.NotesEntity;

import java.util.List;



public class NotesRecycleViewAdapter extends RecyclerView.Adapter<NotesRecycleViewAdapter.ViewHolder> {

    List<NotesEntity> items;
    private NoteClickListener noteClickListener;

    public NotesRecycleViewAdapter(List<NotesEntity> items, NoteClickListener noteClickListener) {
        this.items = items;
        this.noteClickListener = noteClickListener;
    }

    @Override
    public NotesRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note,parent,false);
        return new ViewHolder(view, this.noteClickListener);
    }

    @Override
    public void onBindViewHolder(NotesRecycleViewAdapter.ViewHolder holder, int position) {

        // Note's entity from the db
        NotesEntity note_data = items.get(position);

        // Set note title
        holder.note_title_view.setText(note_data.getNoteText());


        // Set formatted note's modification date to textview
        // (Set creation date if note wasn't modified yet)
        Long date;
        if (note_data.getNoteModificationDate() == 0) {
            date = note_data.getNoteCreationDate();
        }
        else {
            date = note_data.getNoteModificationDate();
        }

        String string_note_date = "<b>" + DateFormat.format("dd-MM-yyyy", date*1000).toString()
                + "</b><br>" + DateFormat.format("HH:mm", date*1000).toString();

        holder.note_date_view.setText(Html.fromHtml(string_note_date));

        // Note's id
        holder.note_id = note_data.getId();
        Log.d("DEBUG_DB", "holder " + holder.note_id);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView note_title_view;
        public TextView note_date_view;
        NoteClickListener noteClickListener;
        int note_id;

        public ViewHolder(View itemView, NoteClickListener noteClickListener) {
            super(itemView);
            note_title_view = itemView.findViewById(R.id.noteTitleView);
            note_date_view = itemView.findViewById(R.id.noteDateView);

            this.noteClickListener = noteClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("DEBUG_DB", "clicked " + this.note_id);
            noteClickListener.onNoteClick(this.note_id);
        }
    }


    public interface NoteClickListener {

        void onNoteClick(int note_id);
    }
}

