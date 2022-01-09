package by.alexandr7035.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Insert
    suspend fun saveNote(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER by id DESC")
    suspend fun getNotesList(): List<NoteEntity>
}