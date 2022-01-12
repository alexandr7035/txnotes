package by.alexandr7035.txnotes.presentation.edit_note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.domain.model.EditNoteModel
import by.alexandr7035.domain.usecase.EditNoteUseCase
import by.alexandr7035.domain.usecase.GetNoteByIdUseCase
import by.alexandr7035.txnotes.core.extensions.getStringDateFromLong
import by.alexandr7035.txnotes.presentation.NoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val editNoteUseCase: EditNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase): ViewModel() {

    private val noteLiveData = MutableLiveData<NoteUiModel>()

    fun getNoteLiveData(): LiveData<NoteUiModel> {
        return noteLiveData
    }

    fun load(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteDomain = getNoteByIdUseCase.execute(noteId)

            val note = NoteUiModel(
                id = noteDomain.id,
                title = noteDomain.title,
                text = noteDomain.text,
                creationDate = noteDomain.creationDate.getStringDateFromLong(NOTE_ITEM_DATE_FORMAT)
            )

            noteLiveData.postValue(note)
        }
    }

    fun editNote(note: EditNoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            editNoteUseCase.execute(note)
        }
    }

    companion object {
        private const val NOTE_ITEM_DATE_FORMAT = "dd MMM yyyy HH:MM:SS"
    }
}