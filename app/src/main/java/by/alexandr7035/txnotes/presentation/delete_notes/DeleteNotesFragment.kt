package by.alexandr7035.txnotes.presentation.delete_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.databinding.FragmentDeleteNotesBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeleteNotesFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentDeleteNotesBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmDeleteBtn.setOnClickListener {
            // Pass the result back to the notes list fragment
            setDeletionDecision(true)

            findNavController().navigateUp()
        }
    }

    // Pass the result back to the previous fragment in backstack
    // Observe it with currentBackStackEntry.savedStateHandle
    private fun setDeletionDecision(deleteNotes: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(getString(R.string.deleted_notes_key), deleteNotes)
    }
}