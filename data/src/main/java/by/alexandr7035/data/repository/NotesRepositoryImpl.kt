package by.alexandr7035.data.repository

import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository

class NotesRepositoryImpl : NotesRepository {
    override fun getNotesList(): List<Note> {
        return listOf(
            Note(1,"Note 1", "lorem ipsum"),
            Note(2, "Note 2", "lorem ipsum more long text"),
            Note(3,"Note 3 LONG TITLE with several lines", "lorem ipsum more long text lorem ipsum more long text lorem ipsum more long textext lorem ipsum more long text lorem ipsum more long text"),
            Note(4, "Note 4", "lorem ipsum more long text lorem ipsum"),
            Note(5, "Short 5", "lorem ipsum")
        )
    }

    override fun getNoteById(id: Int): Note {
        return Note(1, "Note 1", "lorem ipsum note text")
    }
}