package com.example.txnotes;

import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.txnotes.db.NoteEntity;

import java.util.ArrayList;
import java.util.List;


public class NotesRecycleViewAdapter extends RecyclerView.Adapter<NotesRecycleViewAdapter.ViewHolder> {

    private List<NoteEntity> items;
    private List<NoteEntity> selectedItems;

    private NoteClickListener noteClickListener;
    private NoteLongClickListener noteLongClickListener;

    public NotesRecycleViewAdapter(List<NoteEntity> items, NoteClickListener noteClickListener,
                                                            NoteLongClickListener noteLongClickListener) {
        this.items = items;
        this.selectedItems = new ArrayList<>();
        this.noteClickListener = noteClickListener;
        this.noteLongClickListener = noteLongClickListener;
    }

    // Add item to list of selected
    public void selectItem(int position) {
        selectedItems.add(items.get(position));
    }

    // Remove item form list of selected
    public void unselectItem (int position) {
        selectedItems.remove(items.get(position));
    }

    // Remove all items from list of selected
    public void unselectAllItems() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public boolean checkIfItemSelected(int position) {
        if (selectedItems.contains(items.get(position))) {
            return true;
        }
        else {
            return false;
        }
    }

    // Returns true if selectedItems list is not empty
    public boolean checkIfAnyItemSelected() {
        if (selectedItems.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // Returns whole selectedItems list
    public List<NoteEntity> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public NotesRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note,parent,false);
        return new ViewHolder(view, this.noteClickListener, this.noteLongClickListener);
    }

    @Override
    public void onBindViewHolder(NotesRecycleViewAdapter.ViewHolder holder, int position) {

        // Note's entity from the db
        NoteEntity note_data = items.get(position);

        // Set note title
        holder.note_title_view.setText(note_data.getNoteText());

        // Set formatted note's modification date to textview
        // (Set creation date if note wasn't modified yet)
        long date;
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


        // Mark item if selected
        if (checkIfItemSelected(position)) {
            Log.d("DEBUG_TXNOTES", position + " is selected");
            holder.itemView.setBackgroundResource(R.color.colorSelectedNoteWidget);
        }
        else {
            holder.itemView.setBackgroundResource(R.color.colorNoteWidget);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
                            implements View.OnClickListener, View.OnLongClickListener {

        public TextView note_title_view;
        public TextView note_date_view;
        NoteClickListener noteClickListener;
        NoteLongClickListener noteLongClickListener;
        int note_id;

        public ViewHolder(View itemView, NoteClickListener noteClickListener,
                                         NoteLongClickListener noteLongClickListener) {

            super(itemView);
            note_title_view = itemView.findViewById(R.id.noteTitleView);
            note_date_view = itemView.findViewById(R.id.noteDateView);

            this.noteClickListener = noteClickListener;
            this.noteLongClickListener = noteLongClickListener;

            // Use this because ViewHolder implements
            // View.OnClickListener and View.OnLongClickListener interfaces
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("DEBUG_DB", "clicked " + this.note_id);
            noteClickListener.onNoteClick(this.note_id, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("DEBUG_DB", "clicked LONG " + this.note_id);

            // Highlight the deleted item in red
            //view.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorDeletingNote));

            // Select item
            selectItem(getAdapterPosition());

            noteLongClickListener.onLongNoteClick(this.note_id, getAdapterPosition());

            notifyItemChanged(getAdapterPosition());
            //items.remove(getAdapterPosition());
            //notifyItemRemoved(getAdapterPosition());

            return true;
        }

    }


    public interface NoteClickListener {

        void onNoteClick(int note_id, int position);
    }

    public interface NoteLongClickListener {

        void onLongNoteClick(int note_id, int position);
    }
}

