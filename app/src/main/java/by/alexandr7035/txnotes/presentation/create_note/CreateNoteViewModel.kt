package by.alexandr7035.txnotes.presentation.create_note

import androidx.lifecycle.ViewModel
import by.alexandr7035.domain.usecase.CreateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(private val createNoteUseCase: CreateNoteUseCase): ViewModel() {

}