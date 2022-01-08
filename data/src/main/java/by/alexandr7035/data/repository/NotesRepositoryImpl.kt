package by.alexandr7035.data.repository

import by.alexandr7035.domain.model.Note
import by.alexandr7035.domain.repository.NotesRepository

class NotesRepositoryImpl : NotesRepository {
    override fun getNotesList(): List<Note> {
        return listOf(
            Note("Note 1", "lorem ipsum"),
            Note("Note 2", "lorem ipsum more long text"),
            Note("Note 3 LONG TITLE with several lines", "lorem ipsum more long text lorem ipsum more long text lorem ipsum more long textext lorem ipsum more long text lorem ipsum more long text"),
            Note("Note 4", "lorem ipsum more long text lorem ipsum"),
            Note("Short 5", "lorem ipsum")
        )
    }
}