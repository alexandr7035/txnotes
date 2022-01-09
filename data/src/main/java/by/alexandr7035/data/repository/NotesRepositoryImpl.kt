package by.alexandr7035.data.repository

import by.alexandr7035.data.local.NoteEntity
import by.alexandr7035.data.local.NotesDao
import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.domain.model.EditNoteModel
import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository

class NotesRepositoryImpl(private val dao: NotesDao) : NotesRepository {
    override suspend fun getNotesList(): List<Note> {
        val notes = dao.getNotesList()

        val domainNotes = notes.map {
            Note(
                id = it.id,
                title = it.title,
                text = it.text,
                creationDate = it.creationDate
            )
        }

        return domainNotes
    }

    override suspend fun getNoteById(id: Int): Note {
        val note = dao.getNoteById(id)
        return Note(
            id = note.id,
            title = note.title,
            text = note.text,
            creationDate = note.creationDate
        )
    }

    override suspend fun createNote(note: CreateNoteModel) {
        dao.createNote(NoteEntity(
            title = note.title,
            text = note.text,
            creationDate = note.creationDate,
            updateDate = note.creationDate
        ))
    }

    override suspend fun editNote(note: EditNoteModel) {
        dao.editNote(NoteEntity(
            id = note.id,
            title = note.title,
            text = note.text,
            creationDate = note.creationDate,
            updateDate = note.creationDate
        ))
    }
}