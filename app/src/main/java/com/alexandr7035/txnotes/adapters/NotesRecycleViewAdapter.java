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


public class NotesRecycleViewAdapter extends RecyclerView.Adapter<NotesRecycleViewAdapter.ViewHolder> {

    private List<NoteEntity> items;
    private List<NoteEntity> selectedItems;

    private NoteClickListener noteClickListener;
    private NoteLongClickListener noteLongClickListener;

    public NotesRecycleViewAdapter(NoteClickListener noteClickListener,
                                                            NoteLongClickListener noteLongClickListener) {

        this.items = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
        this.noteClickListener = noteClickListener;
        this.noteLongClickListener = noteLongClickListener;
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

        long date;
        date = note_data.getNoteModificationDate();


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

        // If no items selected, show note
        // Else unselect or select the item
        @Override
        public void onClick(View v) {
            Log.d("DEBUG_DB", "clicked " + this.note_id);

            if (checkIfAnyItemSelected()) {
                if (checkIfItemSelected(getAdapterPosition())) {
                    unselectItem(getAdapterPosition());
                } else {
                    selectItem(getAdapterPosition());
                }
            }

            // Open note. See method in MainActivity
            noteClickListener.onNoteClick(this.note_id, getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("DEBUG_DB", "clicked LONG " + this.note_id);

            if (! checkIfItemSelected(getAdapterPosition())) {
                // Select item
                selectItem(getAdapterPosition());
                noteLongClickListener.onLongNoteClick(this.note_id, getAdapterPosition());
            }

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

