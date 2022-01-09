package by.alexandr7035.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert
    suspend fun createNote(note: NoteEntity)

    @Update
    suspend fun editNote(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER by id DESC")
    suspend fun getNotesList(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = (:noteId)")
    suspend fun getNoteById(noteId: Int): NoteEntity
}