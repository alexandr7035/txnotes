package by.alexandr7035.domain.usecase

import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun execute(id: Int): Note {
        return repository.getNoteById(id)
    }
}