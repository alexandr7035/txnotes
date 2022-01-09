package by.alexandr7035.domain.usecase

import by.alexandr7035.domain.extentions.getSubString
import by.alexandr7035.domain.model.EditNoteModel
import by.alexandr7035.domain.repository.NotesRepository
import javax.inject.Inject

class EditNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun execute(note: EditNoteModel) {

        val title = if (note.title.trim().isBlank()) {
            note.text.getSubString(DEFAULT_TITLE_LENGTH)
        } else {
            note.title
        }

        val text = note.text.trim()

        repository.editNote(
            EditNoteModel(
                id = note.id,
                title = title,
                text = text
            )
        )
    }

    companion object {
        private const val DEFAULT_TITLE_LENGTH = 100
    }
}