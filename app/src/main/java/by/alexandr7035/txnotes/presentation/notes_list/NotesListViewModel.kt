package by.alexandr7035.txnotes.presentation.notes_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.domain.usecase.GetNotesListUseCase
import by.alexandr7035.txnotes.core.extensions.getStringDateFromLong
import by.alexandr7035.txnotes.presentation.NoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(private val getNotesListUseCase: GetNotesListUseCase): ViewModel() {
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


    companion object {
        private const val NOTE_ITEM_DATE_FORMAT = "dd MMM yyyy"
    }
}