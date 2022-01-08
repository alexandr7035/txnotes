package by.alexandr7035.txnotes.presentation.notes_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.navigateSafe
import by.alexandr7035.txnotes.databinding.FragmentNotesListBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesListFragment : Fragment(), NoteClickListener {

    private val binding by viewBinding(FragmentNotesListBinding::bind)
    private val viewModel by viewModels<NotesListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NotesAdapter(itemClickListener = this)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = layoutManager

        viewModel.getNotesLiveData().observe(viewLifecycleOwner, {
            Log.d("DEBUG_TAG", "$it")
            adapter.setItems(it)
        })

        viewModel.load()
    }


    override fun onNoteClicked(noteId: Int) {
        findNavController()
            .navigateSafe(NotesListFragmentDirections.actionNotesListFragmentToViewNoteFragment(noteId))
    }
}