package by.alexandr7035.txnotes.presentation.notes_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.domain.model.DeleteNoteModel
import by.alexandr7035.domain.usecase.DeleteNotesUseCase
import by.alexandr7035.domain.usecase.GetNotesListUseCase
import by.alexandr7035.txnotes.core.extensions.getStringDateFromLong
import by.alexandr7035.txnotes.presentation.NoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesListUseCase: GetNotesListUseCase,
    private val deleteNotesUseCase: DeleteNotesUseCase
): ViewModel() {
    private val notesListLiveData = MutableLiveData<List<NoteUiModel>>()

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val notesDomain = getNotesListUseCase.execute()

            val notes = notesDomain.map {
                NoteUiModel(
                    id = it.id,
                    title = it.title,
                    text = it.text,
                    creationDate = it.creationDate.getStringDateFromLong(NOTE_ITEM_DATE_FORMAT)
                )
            }

            notesListLiveData.postValue(notes)
        }
    }

    fun getNotesLiveData(): LiveData<List<NoteUiModel>> = notesListLiveData

    fun deleteNotes(notes: LinkedHashSet<NoteUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            val notesToDelete = notes.map {
                DeleteNoteModel(it.id)
            }

            deleteNotesUseCase.execute(notesToDelete)

            // Reload list from here
            load()
        }
    }


    companion object {
        private const val NOTE_ITEM_DATE_FORMAT = "dd MMM yyyy"
    }
}