package by.alexandr7035.txnotes.presentation.view_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.navigateSafe
import by.alexandr7035.txnotes.databinding.FragmentViewNoteBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewNoteFragment : Fragment() {

    private val binding by viewBinding(FragmentViewNoteBinding::bind)
    private val viewModel by viewModels<ViewNoteViewModel>()
    private val safeArgs by navArgs<ViewNoteFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getNoteLiveData().observe(viewLifecycleOwner, { note ->
            binding.noteTitleView.text = note.title
            binding.noteTextView.text = note.text
            binding.noteDateView.text = note.creationDate

            binding.toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editNoteItem -> {
                        findNavController()
                            .navigateSafe(ViewNoteFragmentDirections.actionViewNoteFragmentToEditNoteFragment(note.id))
                    }
                }

                true
            }
        })

        viewModel.load(safeArgs.noteId)
    }
}