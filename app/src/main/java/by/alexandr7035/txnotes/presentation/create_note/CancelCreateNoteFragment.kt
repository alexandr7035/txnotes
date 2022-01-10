package by.alexandr7035.txnotes.presentation.create_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.navigateSafe
import by.alexandr7035.txnotes.databinding.FragmentCancelCreateNoteBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CancelCreateNoteFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentCancelCreateNoteBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cancel_create_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(
                CancelCreateNoteFragmentDirections
                    .actionCancelCreateNoteFragmentToNotesListFragment())
        }
    }
}