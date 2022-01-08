package by.alexandr7035.txnotes.presentation.view_note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.usecase.GetNoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewNoteViewModel @Inject constructor(private val viewNoteByIdUseCase: GetNoteByIdUseCase): ViewModel() {

    private val noteLiveData = MutableLiveData<Note>()

    fun getNoteLiveData(): LiveData<Note> {
        return noteLiveData
    }

    fun load(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = viewNoteByIdUseCase.execute(noteId)
            noteLiveData.postValue(notes)
        }
    }
}