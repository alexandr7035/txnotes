package by.alexandr7035.domain.usecase

import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository

class GetNotesListUseCase(private val repository: NotesRepository) {
    fun execute(): List<Note> {
        return repository.getNotesList()
    }
}