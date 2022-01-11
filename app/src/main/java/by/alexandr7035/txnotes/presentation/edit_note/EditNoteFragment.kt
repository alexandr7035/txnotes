package by.alexandr7035.txnotes.presentation.edit_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.domain.model.EditNoteModel
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.debug
import by.alexandr7035.txnotes.core.extensions.navigateSafe
import by.alexandr7035.txnotes.core.extensions.showToast
import by.alexandr7035.txnotes.databinding.FragmentEditNoteBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateSafe(EditNoteFragmentDirections
                .actionEditNoteFragmentToCancelEditNoteFragment(safeArgs.noteId))
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


            // Initialize note symbol counters
            val titleActual = oldNoteModel.title.length
            val titleMax = resources.getInteger(R.integer.max_note_title_length)
            binding.titleSymbolsCounter.text = getString(R.string.note_symbol_counter_template, titleActual, titleMax)

            val textActual = oldNoteModel.text.length
            val textMax = resources.getInteger(R.integer.max_note_text_length)
            binding.textSymbolsCounter.text = getString(R.string.note_symbol_counter_template, textActual, textMax)

            binding.noteTitleEditText.doOnTextChanged { text, _, _, _ ->
                binding.titleSymbolsCounter.text = getString(R.string.note_symbol_counter_template, text?.length ?: 0, titleMax)
            }

            binding.noteTextEditText.doOnTextChanged { text, _, _, _ ->
                binding.textSymbolsCounter.text = getString(R.string.note_symbol_counter_template, text?.length ?: 0, textMax)
            }
        })

        viewModel.load(safeArgs.noteId)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.debug("back pressed")
                findNavController().navigateSafe(EditNoteFragmentDirections
                    .actionEditNoteFragmentToCancelEditNoteFragment(safeArgs.noteId))
            }
        })
    }
}