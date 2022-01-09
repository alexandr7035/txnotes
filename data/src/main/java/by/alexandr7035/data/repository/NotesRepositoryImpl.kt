package by.alexandr7035.data.repository

import by.alexandr7035.data.local.NoteEntity
import by.alexandr7035.data.local.NotesDao
import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository

class NotesRepositoryImpl(private val dao: NotesDao) : NotesRepository {
    override suspend fun getNotesList(): List<Note> {
        val notes = dao.getNotesList()

        val domainNotes = notes.map {
            Note(
                id = it.id,
                title = it.title,
                text = it.text
            )
        }

        return domainNotes
    }

    override suspend fun getNoteById(id: Int): Note {
        val note = dao.getNoteById(id)
        return Note(id = note.id, title = note.title, text = note.text)
    }

    override suspend fun saveNote(note: CreateNoteModel) {
        dao.saveNote(NoteEntity(
            title = note.title,
            text = note.text
        ))
    }
}