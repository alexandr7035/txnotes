package by.alexandr7035.txnotes.presentation.notes_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.usecase.GetNotesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(private val getNotesListUseCase: GetNotesListUseCase): ViewModel() {
    private val notesListLiveData = MutableLiveData<List<Note>>()

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = getNotesListUseCase.execute()
            notesListLiveData.postValue(notes)
        }
    }

    fun getNotesLiveData(): LiveData<List<Note>> = notesListLiveData
}