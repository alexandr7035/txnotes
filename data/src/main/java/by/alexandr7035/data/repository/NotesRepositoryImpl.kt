package by.alexandr7035.data.repository

import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository

class NotesRepositoryImpl: NotesRepository {
    override fun getNotesList(): List<Note> {
        return listOf(
            Note("Note 1", "lorem ipsum"),
            Note("Note 2", "lorem ipsum"),
        )
    }
}