package by.alexandr7035.domain.repository

import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.domain.model.DeleteNoteModel
import by.alexandr7035.domain.model.EditNoteModel
import by.alexandr7035.domain.model.Note

interface NotesRepository {
    suspend fun getNotesList(): List<Note>

    suspend fun getNoteById(id: Int): Note

    suspend fun createNote(note: CreateNoteModel)

    suspend fun editNote(note: EditNoteModel)

    suspend fun deleteNotes(notes: List<DeleteNoteModel>)
}