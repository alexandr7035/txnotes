package com.example.txnotes;

import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.txnotes.db.NotesEntity;

import java.util.List;




public class NotesRecycleViewAdapter extends RecyclerView.Adapter<NotesRecycleViewAdapter.ViewHolder> {

    List<NotesEntity> items;

    public NotesRecycleViewAdapter(List<NotesEntity> items) {
        this.items = items;
    }

    @Override
    public NotesRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesRecycleViewAdapter.ViewHolder holder, int position) {
        // Set title
        holder.name.setText(items.get(position).getNoteText());

        // Set formatted note's modification date to textview
        Long date = items.get(position).getNoteModificationDate();
        String string_note_date = "<b>" + DateFormat.format("dd-MM-yyyy", date*1000).toString()
                + "</b><br>" + DateFormat.format("HH:mm", date*1000).toString();

        holder.time.setText(Html.fromHtml(string_note_date));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.noteTitleView);
            time= itemView.findViewById(R.id.noteDateView);
        }
    }
}