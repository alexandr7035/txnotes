package by.alexandr7035.txnotes.presentation.notes_list

// model for list with isSelected property
data class NoteListItemUiModel(
    val id: Int,
    val title: String,
    val text: String,
    val creationDate: String,
    var isSelected: Boolean
)