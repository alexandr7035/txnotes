package by.alexandr7035.txnotes.presentation.notes_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.domain.model.Note
import by.alexandr7035.txnotes.databinding.ViewNoteItemBinding

class NotesAdapter: RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var items: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ViewNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Note>) {
        this.items = items
    }

    inner class NoteViewHolder(private val binding: ViewNoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.noteTitleView.text = note.title
            binding.noteTextView.text = note.text
        }
    }
}