package by.alexandr7035.txnotes.presentation.notes_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.txnotes.databinding.ViewNoteItemBinding
import by.alexandr7035.txnotes.presentation.NoteUiModel

class NotesAdapter(
    private val itemClickListener: NoteClickListener,
    private val itemLongClickListener: NoteLongClickListener
): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var items: List<NoteUiModel> = emptyList()
    private var selectedItems: LinkedHashSet<NoteUiModel> = LinkedHashSet()

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

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        this.selectedItems = LinkedHashSet()
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(private val binding: ViewNoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteUiModel) {
            binding.noteTitleView.text = note.title
            binding.noteTextView.text = note.text
            binding.noteDateView.text = note.creationDate

            binding.root.setOnClickListener {

                // If note is selected just unselect it
                if (selectedItems.contains(note)) {
                    selectedItems.remove(note)
                    binding.selectionMark.visibility = View.INVISIBLE
                }
                // Pass click otherwise
                else {
                    itemClickListener.onNoteClicked(note.id)
                }
            }

            binding.root.setOnLongClickListener {
                if (selectedItems.contains(note)) {
                    selectedItems.remove(note)
                    binding.selectionMark.visibility = View.INVISIBLE
                }
                else {
                    selectedItems.add(note)
                    binding.selectionMark.visibility = View.VISIBLE
                }

                true
            }
        }
    }
}