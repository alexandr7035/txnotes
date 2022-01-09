package by.alexandr7035.txnotes.presentation.create_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.usecase.CreateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(private val createNoteUseCase: CreateNoteUseCase): ViewModel() {
    fun saveNote(note: CreateNoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            createNoteUseCase.execute(note)
        }
    }
}