package by.alexandr7035.domain.usecase

import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    fun execute(note: Note) {
        repository.saveNote(note)
    }
}