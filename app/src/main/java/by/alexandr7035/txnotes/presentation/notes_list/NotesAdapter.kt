package by.alexandr7035.txnotes.presentation.notes_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.txnotes.databinding.ViewNoteItemBinding
import by.alexandr7035.txnotes.presentation.NoteUiModel

class NotesAdapter(
    private val itemClickListener: NoteClickListener,
    private val itemLongClickListener: NoteLongClickListener,
    private val itemClickListenerWithSelection: NoteClickListenerWithSelection
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var items: List<NoteUiModel> = emptyList()
    private var selectedItems: LinkedHashSet<NoteUiModel> = LinkedHashSet()

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
    fun setItems(items: List<NoteUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun clearSelection() {
        this.selectedItems = LinkedHashSet()
    }

    fun getSelectedNotes(): LinkedHashSet<NoteUiModel> {
        return selectedItems
    }

    fun checkIfAnyItemSelected(): Boolean {
        return selectedItems.isNotEmpty()
    }

    inner class NoteViewHolder(private val binding: ViewNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteUiModel, itemPosition: Int) {
            binding.noteTitleView.text = note.title
            binding.noteTextView.text = note.text
            binding.noteDateView.text = note.creationDate

            if (selectedItems.contains(note)) {
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


        private fun toggleNoteSelection(note: NoteUiModel) {
            if (selectedItems.contains(note)) {
                selectedItems.remove(note)
            } else {
                selectedItems.add(note)
            }
        }
    }
}