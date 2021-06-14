package com.alexandr7035.txnotes.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Vibrator
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.txnotes.BuildConfig
import com.alexandr7035.txnotes.R
import com.alexandr7035.txnotes.adapters.NotesAdapter
import com.alexandr7035.txnotes.adapters.NotesAdapter.NoteClickListener
import com.alexandr7035.txnotes.adapters.NotesAdapter.NoteLongClickListener
import com.alexandr7035.txnotes.databinding.ActivityMainBinding
import com.alexandr7035.txnotes.db.NoteEntity
import com.alexandr7035.txnotes.dialogs.ExportNotesConformationDialog
import com.alexandr7035.txnotes.dialogs.ExportNotesConformationDialog.DialogActionHandler
import com.alexandr7035.txnotes.dialogs.VersionChangesDialog
import com.alexandr7035.txnotes.utils.NoteToTxtSaver.Companion.saveNotesToTxt
import com.alexandr7035.txnotes.utils.NotesSorter
import com.alexandr7035.txnotes.utils.SortingState
import com.alexandr7035.txnotes.viewmodel.MainViewModel
import com.alexandr7035.txnotes.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private val LOG_TAG = "DEBUG_TXNOTES"

    private lateinit var defaultClickListener: DefaultClickListener
    private lateinit var selectionClickListener: SelectionClickListener

    private lateinit var vibrator: Vibrator

    private lateinit  var notesListLiveData: LiveData<List<NoteEntity>>
    private lateinit  var notesCountLiveData: LiveData<Int>
    private lateinit  var sortingStateLiveData: MutableLiveData<String>
    private lateinit  var viewModel: MainViewModel
    private lateinit  var selectedNotesLiveData: MutableLiveData<List<NoteEntity>>
    private lateinit var selectedNotes: List<NoteEntity>
    private  lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchVisibleLiveData: MutableLiveData<Boolean>

    private lateinit var adapter: NotesAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        //Log.d(LOG_TAG, "start the app. Version: " + BuildConfig.VERSION_CODE);
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Init vibrator
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        // Init SharedPreferences
        sharedPreferences = getPreferences(MODE_PRIVATE)

        // Hide delete note button by default
        binding.deleteNoteButton.hide()

        // Toolbar
        binding.toolbar.inflateMenu(R.menu.menu_main_activity_toolbar)
        binding.toolbar.setOnMenuItemClickListener(this)

        binding.closeSearchBtn.setOnClickListener {
            searchVisibleLiveData.postValue(false)
        }

        // Disable submenu title
        binding.toolbar.menu.findItem(R.id.item_sort_submenu).subMenu.clearHeader()
        // Make sorting items checkable
        binding.toolbar.menu.findItem(R.id.item_sort_submenu).subMenu.setGroupCheckable(0, true, true)

        // Init recyclerview
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Click listeners for recyclerview items
        selectionClickListener = SelectionClickListener()
        defaultClickListener = DefaultClickListener()

        adapter = NotesAdapter()

        // Set default click listeners
        adapter.setItemClickListener(defaultClickListener)
        adapter.setItemLongClickListener(defaultClickListener)
        binding.recyclerView.adapter = adapter


        // ViewModel & livedata
        viewModel = ViewModelProvider(this, MainViewModelFactory(this.application)).get(
            MainViewModel::class.java
        )
        notesListLiveData = viewModel.notesList
        notesCountLiveData = viewModel.notesCount
        selectedNotesLiveData = viewModel.selectedNotesLiveData
        sortingStateLiveData = MutableLiveData()
        searchVisibleLiveData = MutableLiveData(false)
        selectedNotes = ArrayList()

        // Watch for notes list and update the UI
        notesListLiveData.observe(this, { notes ->

                if (notes != null) {
                    val sortingMode = sharedPreferences.getString(
                        getString(R.string.shared_pref_key_sorting),
                        "SORT_BY_MDATE_DESC"
                    )
                    adapter.items = notes
                    sortingStateLiveData.postValue(sortingMode)
                }
        })

        // Watch for notes count and update the ui
        notesCountLiveData.observe(this, { notesCount ->
            // Change toolbar title
            binding.toolbarTitle.text = getString(R.string.activity_main_title, notesCount)
        })

        // Watch fr notes selection and update the ui
        selectedNotesLiveData.observe(this, { selectedNotes ->
            // //Log.d(LOG_TAG, "selected notes livedata state" + selectedNotes.toString());
            if (selectedNotes != null) {
                if (selectedNotes.isNotEmpty()) {
                    //Log.d(LOG_TAG, "some items are selected: " + selectedNotes);
                    binding.createNoteButton.hide()
                    binding.deleteNoteButton.show()

                    //Log.d(LOG_TAG, "set selection click listener");
                    adapter.setItemClickListener(selectionClickListener)
                    adapter.setItemLongClickListener(selectionClickListener)
                } else {
                    //Log.d(LOG_TAG, "no items selected now");
                    binding.deleteNoteButton.hide()
                    binding.createNoteButton.show()

                    //Log.d(LOG_TAG, " set default click listener");
                    adapter.setItemClickListener(defaultClickListener)
                    adapter.setItemLongClickListener(defaultClickListener)
                }
            }
        })


        // Livedata that updates sorting
        sortingStateLiveData.observe(this, { sortingState ->
            //Log.d(LOG_TAG, "sorting state changed '" + sortingState + "'");
            val sortByMdateNewFirstItem = binding.toolbar.menu.findItem(R.id.item_sort_by_mdtate_new_first)
            val sortByMdateOldFirstItem = binding.toolbar.menu.findItem(R.id.item_sort_by_mdtate_old_first)
            val sortByTitleItem = binding.toolbar.menu.findItem(R.id.item_sort_by_title)

            if (sortingState != null) {
                when (sortingState) {
                    SortingState.SORT_BY_MDATE_DESC -> {
                        sortByMdateNewFirstItem.isChecked = true
                        NotesSorter.sortByModificationDateDesc(adapter.items)
                    }
                    SortingState.SORT_BY_MDATE -> {
                        sortByMdateOldFirstItem.isChecked = true
                        NotesSorter.sortByModificationDate(adapter.items)
                    }
                    SortingState.SORT_BY_TEXT -> {
                        sortByTitleItem.isChecked = true
                        NotesSorter.sortByTitle(adapter.items)
                    }
                }


                // Save sorting state in memory
                val prefEditor = sharedPreferences.edit()
                prefEditor.putString(
                    getString(R.string.shared_pref_key_sorting),
                    sortingState
                )
                prefEditor.apply()
            }
        })

        searchVisibleLiveData.observe(this, { searchVisible ->

            if (searchVisible) {
                //Log.d(LOG_TAG, "show search view");

                // Hide all menu items
                for (i in 0 until binding.toolbar.menu.size())
                    binding.toolbar.menu.getItem(i).isVisible = false

                binding.toolbarTitle.visibility = View.GONE
                binding.searchView.visibility = View.VISIBLE
            } else {
                // Show all items

                //Log.d(LOG_TAG, "hide search view");
                for (i in 0 until binding.toolbar.menu.size())
                    binding.toolbar.menu.getItem(i).isVisible = true

                binding.toolbarTitle.visibility = View.VISIBLE
                binding.searchView.visibility = View.GONE
                binding.searchEditText.setText("")
                binding.searchNothingFoundView.visibility = View.GONE
            }
        })

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val filteredList = ArrayList<NoteEntity>()
                val notesList = notesListLiveData.value as ArrayList<NoteEntity>? ?: return
                if (charSequence.toString() != "") {
                    for (note in notesList) {
                        if (note.noteTitle.toLowerCase(Locale.ROOT).contains(charSequence.toString().toLowerCase(Locale.ROOT).trim {
                                it <= ' '
                        })
                        ) {
                            filteredList.add(note)
                        }
                    }

                    // Check if empty and show message
                    if (filteredList.isEmpty()) {
                        //Log.d(LOG_TAG, "nothing found");
                        binding.searchNothingFoundView.visibility = View.VISIBLE
                    } else {
                        binding.searchNothingFoundView.visibility = View.GONE
                    }
                    adapter.items = filteredList
                } else {
                    adapter.items = notesList
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })


        // Show 'what's new' dialog if current version run for the first time
        val lastInstalledVersion =
            sharedPreferences.getInt(getString(R.string.shared_pref_last_installed_version), 0)

        // DEBUG
        //lastInstalledVersion = 0;
        if (lastInstalledVersion != BuildConfig.VERSION_CODE) {
            //Log.d(LOG_TAG, "run version " + BuildConfig.VERSION_CODE + " for the first time, show release info");

            // Show dialog
            val fm = supportFragmentManager
            val dialog = VersionChangesDialog()
            dialog.show(fm, "version_change_dialog")
            val prefEditor = sharedPreferences.edit()
            prefEditor.putInt(
                getString(R.string.shared_pref_last_installed_version),
                BuildConfig.VERSION_CODE
            )
            prefEditor.apply()
        }
    }

    // Default click listener for recyclerview items
    internal inner class DefaultClickListener : NoteClickListener, NoteLongClickListener {
        override fun onNoteClick(skill_id: Long, position: Int) {
            ////Log.d(LOG_TAG, "clicked position " + position + " id " + skill_id);

            //Log.d(LOG_TAG, "called default onCLick");
            //Log.d(LOG_TAG, "start new activity (SHOW)");
            val intent = Intent(this@MainActivity, NoteActivity::class.java)
            intent.putExtra("passed_note_id", skill_id)
            startActivity(intent)
        }

        override fun onLongNoteClick(skill_id: Long, position: Int) {
            ////Log.d(LOG_TAG, "clicked (LONG) position " + position + " id " + skill_id);
            adapter.selectItem(position)
            selectedNotesLiveData.postValue(adapter.selectedItems)
        }
    }

    // Set if at least one item in RecyclerView is selected
    // Replaced by default click listener when no items selected
    internal inner class SelectionClickListener : NoteClickListener, NoteLongClickListener {
        override fun onNoteClick(skill_id: Long, position: Int) {
            ////Log.d(LOG_TAG, "SELECTED_CL: click item " + position + " skill_id " + skill_id);

            // Select item if not selected
            // Else unselect

            //Log.d(LOG_TAG, "ON_CLICK SELECTION CALLED");
            if (adapter.checkIfItemSelected(position)) {
                adapter.unselectItem(position)
            } else {
                adapter.selectItem(position)
            }

            // Update livedata
            selectedNotesLiveData.postValue(adapter.selectedItems)
        }

        override fun onLongNoteClick(skill_id: Long, position: Int) {
            ////Log.d(LOG_TAG, "SELECTED_CL: LONG click item " + position + " skill_id " + skill_id);

            // Do nothing
            // May be changed later
        }
    }

    // DeleteNoteButton
    fun deleteNoteBtnAction(v: View) {
        val deletingNotesCount = adapter.selectedItems.size

        // Snackbar to show deleted notes count and 'undo' button
        val text = getString(R.string.snack_delete_notes, "" + deletingNotesCount)

        val snackBar = Snackbar.make(binding.mainLayout, text, Snackbar.LENGTH_LONG)

        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    //Yes button clicked

                    // Delete notes
                    for (note in adapter.selectedItems) {
                        // Remove from db
                        viewModel.removeNote(note)
                    }

                    // Clear selection
                    adapter.unselectAllItems()
                    selectedNotesLiveData.value = adapter.selectedItems

                    // Hide searchView if shown
                    if (searchVisibleLiveData.value!!) {
                        searchVisibleLiveData.postValue(false)
                    }
                    vibrator.vibrate(100)
                    snackBar.show()
                    adapter.unselectAllItems()
                    selectedNotesLiveData.setValue(adapter.selectedItems)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    adapter.unselectAllItems()
                    selectedNotesLiveData.setValue(adapter.selectedItems)
                }
            }
        }
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(
            Html.fromHtml(
                getString(
                    R.string.dialog_delete_note_message,
                    "" + deletingNotesCount
                )
            )
        )
        builder.setPositiveButton(
            getString(R.string.dialog_delete_note_positive),
            dialogClickListener
        )
        builder.setNegativeButton(
            getString(R.string.dialog_delete_note_negative),
            dialogClickListener
        )
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val msgTxt = dialog.findViewById<TextView>(android.R.id.message)
        val posBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val negBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        msgTxt!!.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.dialog_message_text)
        )
        posBtn.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.dialog_message_btn)
        )
        negBtn.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.dialog_message_btn)
        )
    }

    // Override back button in MainActivity
    // Minimize the app if no items selected
    // Else clear selection
    override fun onBackPressed() {

        // If items selected unselect them
        if (adapter.checkIfAnyItemSelected()) {
            adapter.unselectAllItems()
            selectedNotesLiveData.setValue(adapter.selectedItems)
        } else if (searchVisibleLiveData.value!!) {
            searchVisibleLiveData.postValue(false)
        } else {
            super.onBackPressed()
        }
    }

    // Shows NewNoteActivity
    fun createNewNoteAction(v: View) {
        // Go to CreateNewNoteActivity
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
    }

    // A click handler for toolbar menu items
    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_sort_by_mdtate_new_first -> { //Log.d(LOG_TAG, "sort by mdate desc clicked");
                sortingStateLiveData.postValue("SORT_BY_MDATE_DESC")
            }
            R.id.item_sort_by_mdtate_old_first -> { //Log.d(LOG_TAG, "sort by mdate clicked");
                sortingStateLiveData.postValue("SORT_BY_MDATE")
            }
            R.id.item_sort_by_title -> { //Log.d(LOG_TAG, "sort by text clicked");
                sortingStateLiveData.postValue("SORT_BY_TEXT")
            }
            R.id.item_exit -> {
                finish()
            }
            R.id.item_search -> {
                searchVisibleLiveData.postValue(true)
            }
            R.id.item_export_to_txt -> {
                showToast("Save files to Downloads/TXNotes")

                // Show dialog
                val fm = supportFragmentManager
                val dialog = ExportNotesConformationDialog()
                dialog.show(fm, "export_confirmation")
                dialog.setActionHandler(object : DialogActionHandler {
                    override fun onPositiveClick() {
                        // FIXME
                        val notes = notesListLiveData.value
                        if (notes != null) {
                            saveNotesToTxt(this@MainActivity, notes)
                        }
                        dialog.dismiss()
                    }

                    override fun onNegativeClick() {
                        dialog.dismiss()
                    }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}