package com.alexandr7035.txnotes.adapters;

import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alexandr7035.txnotes.R;
import com.alexandr7035.txnotes.db.NoteEntity;

import java.util.ArrayList;
import java.util.List;


public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private List<NoteEntity> items;
    private List<NoteEntity> selectedItems;

    private NoteClickListener noteClickListener;
    private NoteLongClickListener noteLongClickListener;

    private final String LOG_TAG = "DEBUG_TXNOTES";

    public NotesRecyclerViewAdapter() {

        this.items = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
    }

    public void setItems(List<NoteEntity> items) {
        this.items = items;
        Log.d("DEBUG_TXNOTES", "SETITEMS CALLED " + items.toString());
        notifyDataSetChanged();
    }

    // Add item to list of selected
    public void selectItem(int position) {
        selectedItems.add(items.get(position));
        notifyItemChanged(position);
    }

    // Remove item form list of selected
    public void unselectItem (int position) {
        selectedItems.remove(items.get(position));
        notifyItemChanged(position);
    }

    // Remove all items from list of selected
    public void unselectAllItems() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public boolean checkIfItemSelected(int position) {
        return selectedItems.contains(items.get(position));
    }

    // Returns true if selectedItems list is not empty
    public boolean checkIfAnyItemSelected() {
        return selectedItems.size() > 0;
    }

    // Returns whole selectedItems list
    public List<NoteEntity> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public NotesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesRecyclerViewAdapter.ViewHolder holder, int position) {

        // Note's entity from the db
        NoteEntity note_data = items.get(position);

        // Set note title
        holder.note_title_view.setText(note_data.getNoteText());

        // Set formatted note's modification date to textview

        long date;
        if  (note_data.getNoteModificationDate() == 0) {
            date = note_data.getNoteCreationDate();
        }
        else  {
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
            holder.itemView.setBackgroundResource(R.drawable.note_view_background_selected);
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.note_view_background);
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

        int note_id;

        public ViewHolder(View itemView) {

            super(itemView);
            note_title_view = itemView.findViewById(R.id.noteTitleView);
            note_date_view = itemView.findViewById(R.id.noteDateView);

            // Use this because ViewHolder implements
            // View.OnClickListener and View.OnLongClickListener interfaces
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {

            noteClickListener.onNoteClick(note_id, getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View view) {

            noteLongClickListener.onLongNoteClick(note_id, getAdapterPosition());
            return true;
        }

    }


    public interface NoteClickListener {

        void onNoteClick(int note_id, int position);
    }

    public interface NoteLongClickListener {

        void onLongNoteClick(int note_id, int position);
    }

    // Allows to change click listener
    // For simple selection implementation
    public void setItemClickListener(NoteClickListener noteClickListener) {
        Log.d(LOG_TAG, "CHANGE CLICK LISTENER");
        this.noteClickListener = noteClickListener;

    }

    public void setItemLongClickListener(NoteLongClickListener noteLongClickListener) {
        Log.d(LOG_TAG, "CHANGE LONG CLICK LISTENER");
        this.noteLongClickListener = noteLongClickListener;

    }

}

