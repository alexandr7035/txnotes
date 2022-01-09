package by.alexandr7035.txnotes.presentation.edit_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.domain.model.EditNoteModel
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.showToast
import by.alexandr7035.txnotes.databinding.FragmentEditNoteBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    private val binding by viewBinding(FragmentEditNoteBinding::bind)
    private val viewModel by viewModels<EditNoteViewModel>()
    private val safeArgs by navArgs<EditNoteFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO exit confirmation dialog
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getNoteLiveData().observe(viewLifecycleOwner, { oldNoteModel ->

            binding.noteTitleEditText.setText(oldNoteModel.title)
            binding.noteTextEditText.setText(oldNoteModel.text)

            // Edit note
            binding.toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.saveNoteItem -> {
                        if (binding.noteTextEditText.text.isBlank()) {
                            // TODO error dialog
                            requireContext().showToast(getString(R.string.error_empty_note))
                        } else {
                            val note = EditNoteModel (
                                id = oldNoteModel.id,
                                title = binding.noteTitleEditText.text.toString(),
                                text = binding.noteTextEditText.text.toString(),
                            )

                            viewModel.editNote(note)

                            findNavController().navigateUp()
                        }
                    }
                }

                true
            }
        })

        viewModel.load(safeArgs.noteId)
    }
}