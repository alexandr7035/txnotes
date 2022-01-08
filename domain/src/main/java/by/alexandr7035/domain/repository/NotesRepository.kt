package by.alexandr7035.domain.repository

import by.alexandr7035.domain.model.Note

interface NotesRepository {
    fun getNotesList(): List<Note>

    fun getNoteById(id: Int): Note
}