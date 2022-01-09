package by.alexandr7035.domain.usecase

import by.alexandr7035.domain.model.CreateNoteModel
import by.alexandr7035.domain.repository.NotesRepository
import by.alexandr7035.domain.extentions.getSubString
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun execute(note: CreateNoteModel) {

        val title = if (note.title.trim().isBlank()) {
            note.text.getSubString(DEFAULT_TITLE_LENGTH)
        }
        else {
            note.title
        }

        val text = note.text.trim()

        repository.saveNote(CreateNoteModel(
            title = title,
            text = text,
            creationDate = note.creationDate
        ))
    }

    companion object {
        private const val DEFAULT_TITLE_LENGTH = 100
    }
}