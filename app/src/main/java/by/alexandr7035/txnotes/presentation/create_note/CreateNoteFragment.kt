package by.alexandr7035.txnotes.presentation.create_note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.domain.model.Note
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.showToast
import by.alexandr7035.txnotes.databinding.FragmentCreateNoteBinding
import by.alexandr7035.txnotes.databinding.FragmentViewNoteBinding
import by.alexandr7035.txnotes.presentation.view_note.ViewNoteViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNoteFragment : Fragment() {

    private val binding by viewBinding(FragmentCreateNoteBinding::bind)
    private val viewModel by viewModels<CreateNoteViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.saveNoteItem -> {
                    if (binding.noteTextView.text.isBlank()) {
                        // TODO error dialog
                        requireContext().showToast(getString(R.string.error_empty_note))
                    }
                    else {
                        val note = CreateNoteModel(
                            title = binding.noteTitleView.text.toString(),
                            text = binding.noteTextView.text.toString(),
                            creationDate = System.currentTimeMillis()
                        )

                        viewModel.saveNote(note)

                        findNavController().navigateUp()
                    }
                }
            }

            true
        }
    }
}