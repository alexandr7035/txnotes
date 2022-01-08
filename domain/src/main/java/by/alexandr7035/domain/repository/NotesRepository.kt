package by.alexandr7035.domain.repository

import by.alexandr7035.domain.model.Note

interface NotesRepository {
    suspend fun getNotesList(): List<Note>

    suspend fun getNoteById(id: Int): Note

    suspend fun saveNote(note: Note)
}