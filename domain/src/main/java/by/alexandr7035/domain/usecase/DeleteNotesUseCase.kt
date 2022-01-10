package by.alexandr7035.domain.usecase

import by.alexandr7035.domain.model.DeleteNoteModel
import by.alexandr7035.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteNotesUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun execute(notes: List<DeleteNoteModel>) {
        repository.deleteNotes(notes)
    }
}