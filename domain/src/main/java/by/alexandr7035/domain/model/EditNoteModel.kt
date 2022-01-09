package by.alexandr7035.domain.model

data class EditNoteModel(
    val id: Int,
    val title: String,
    val text: String,
    val creationDate: Long
)