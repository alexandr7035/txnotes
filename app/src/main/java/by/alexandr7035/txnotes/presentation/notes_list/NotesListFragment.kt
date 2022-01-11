package by.alexandr7035.txnotes.presentation.notes_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.alexandr7035.txnotes.R
import by.alexandr7035.txnotes.core.extensions.checkIfBackStackEmpty
import by.alexandr7035.txnotes.core.extensions.debug
import by.alexandr7035.txnotes.core.extensions.navigateSafe
import by.alexandr7035.txnotes.databinding.FragmentNotesListBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NotesListFragment : Fragment(), NoteClickListener, NoteLongClickListener, NoteClickListenerWithSelection {

    private val binding by viewBinding(FragmentNotesListBinding::bind)
    private val viewModel by viewModels<NotesListViewModel>()

    private val adapter by lazy {
        NotesAdapter(this, this, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = layoutManager

        viewModel.getNotesLiveData().observe(viewLifecycleOwner, {
            Timber.debug("update livedata $it")
            adapter.setItems(it)
        })

        viewModel.load()

        binding.createNoteBtn.setOnClickListener {
            findNavController()
                .navigateSafe(NotesListFragmentDirections.actionNotesListFragmentToCreateNoteFragment())
        }

        // Show deletion confirmation dialog. The result will be passed back via livedata
        binding.deleteNoteBtn.setOnClickListener {
            findNavController().navigateSafe(NotesListFragmentDirections
                .actionNotesListFragmentToDeleteNotesFragment())
        }

        // Here the result of notes deletion will be passed back
        val key = getString(R.string.deleted_notes_key)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(key)?.observe(viewLifecycleOwner, { deleteNotes ->
            Timber.debug("get notes deletion request from dialog")
            if (deleteNotes) {
                // Delete notes
                val notesToDelete = adapter.getSelectedNotes()
                viewModel.deleteNotes(notesToDelete)

                // Clear selection
                adapter.clearSelection()
                toggleFab()
            }
        })

        // Clear notes selection on back pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                when {
                    adapter.checkIfAnyItemSelected() -> {
                        // Clear selection
                        adapter.clearSelection()
                        toggleFab()
                    }
                    // The default behaviour
                    navHostFragment.checkIfBackStackEmpty() -> {
                        requireActivity().finish()
                    }
                    else -> {
                        findNavController().navigateUp()
                    }
                }

            }
        })
    }


    override fun onNoteClicked(noteId: Int) {
        findNavController()
            .navigateSafe(NotesListFragmentDirections.actionNotesListFragmentToViewNoteFragment(noteId))
    }

    override fun onNoteLongClicked(noteId: Int) {
        toggleFab()
    }

    // Click on ANY note if selection is applied passed here
    // In order to toggle FABs when selection is cleared
    override fun onNoteClickedWithSelection() {
        toggleFab()
    }

    private fun toggleFab() {
        binding.createNoteBtn.isVisible = ! adapter.checkIfAnyItemSelected()
        binding.deleteNoteBtn.isVisible = adapter.checkIfAnyItemSelected()
    }

}