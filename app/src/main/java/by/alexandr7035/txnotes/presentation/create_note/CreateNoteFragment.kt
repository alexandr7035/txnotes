package by.alexandr7035.txnotes.presentation.create_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.navigateSafe
import by.alexandr7035.txnotes.core.extensions.showToast
import by.alexandr7035.txnotes.databinding.FragmentCreateNoteBinding
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
            navigateBack()
        }

        binding.toolbar.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.saveNoteItem -> {
                    if (binding.noteTextView.text.isBlank() && binding.noteTitleView.text.isBlank()) {
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        })

        // Initialize note symbol counters
        val titleActual = 0
        val titleMax = resources.getInteger(R.integer.max_note_title_length)
        binding.titleSymbolsCounter.text = getString(R.string.note_symbol_counter_template, titleActual, titleMax)

        val textActual = 0
        val textMax = resources.getInteger(R.integer.max_note_text_length)
        binding.textSymbolsCounter.text = getString(R.string.note_symbol_counter_template, textActual, textMax)

        binding.noteTitleView.doOnTextChanged { text, _, _, _ ->
            binding.titleSymbolsCounter.text = getString(R.string.note_symbol_counter_template, text?.length ?: 0, titleMax)
        }

        binding.noteTextView.doOnTextChanged { text, _, _, _ ->
            binding.textSymbolsCounter.text = getString(R.string.note_symbol_counter_template, text?.length ?: 0, textMax)
        }
    }


    private fun navigateBack() {
        if (binding.noteTitleView.text.isNotBlank() || binding.noteTextView.text.isNotBlank()) {
            findNavController().navigateSafe(
                CreateNoteFragmentDirections.actionCreateNoteFragmentToCancelCreateNoteFragment())
        }
        else {
            findNavController().popBackStack()
        }
    }
}