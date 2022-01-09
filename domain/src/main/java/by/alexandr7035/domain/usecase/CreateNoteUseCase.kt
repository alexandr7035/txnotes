package by.alexandr7035.domain.usecase

import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun execute(note: CreateNoteModel) {
        repository.saveNote(note)
    }
}