package by.alexandr7035.txnotes.presentation.notes_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.txnotes.databinding.ViewNoteItemBinding

class NotesAdapter(
    private val itemClickListener: NoteClickListener,
    private val itemLongClickListener: NoteLongClickListener,
    private val itemClickListenerWithSelection: NoteClickListenerWithSelection
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var items: List<NoteListItemUiModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ViewNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<NoteListItemUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        items.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    fun getSelectedNotes(): List<NoteListItemUiModel> {
        return items.filter { it.isSelected }
    }

    fun checkIfAnyItemSelected(): Boolean {
        return items.findLast { it.isSelected } != null
    }

    inner class NoteViewHolder(private val binding: ViewNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteListItemUiModel, itemPosition: Int) {
            binding.noteTitleView.text = note.title
            binding.noteTextView.text = note.text
            binding.noteDateView.text = note.creationDate

            if (note.isSelected) {
                binding.selectionMark.visibility = View.VISIBLE
            }
            else {
                binding.selectionMark.visibility = View.INVISIBLE
            }

            binding.root.setOnClickListener {
                if (checkIfAnyItemSelected()) {
                    toggleNoteSelection(note)
                    notifyItemChanged(itemPosition)
                    itemClickListenerWithSelection.onNoteClickedWithSelection()
                }
                else {
                    itemClickListener.onNoteClicked(note.id)
                }
            }

            binding.root.setOnLongClickListener {
                toggleNoteSelection(note)
                notifyItemChanged(itemPosition)
                itemLongClickListener.onNoteLongClicked(note.id)
                true
            }
        }


        private fun toggleNoteSelection(note: NoteListItemUiModel) {
            note.isSelected = ! note.isSelected
        }
    }
}