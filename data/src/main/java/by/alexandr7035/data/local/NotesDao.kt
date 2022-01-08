package by.alexandr7035.data.local

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface NotesDao {
    @Insert
    fun saveNote(note: NoteEntity)
}