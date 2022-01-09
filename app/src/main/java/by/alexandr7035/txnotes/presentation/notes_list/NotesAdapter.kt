package by.alexandr7035.txnotes.presentation.notes_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.txnotes.databinding.ViewNoteItemBinding
import by.alexandr7035.txnotes.presentation.NoteUiModel

class NotesAdapter(private val itemClickListener: NoteClickListener): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var items: List<NoteUiModel> = emptyList()

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

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<NoteUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(private val binding: ViewNoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteUiModel) {
            binding.noteTitleView.text = note.title
            binding.noteTextView.text = note.text
            // FIXME remove
            binding.noteDateView.text = note.creationDate

            binding.root.setOnClickListener {
                itemClickListener.onNoteClicked(note.id)
            }
        }
    }
}