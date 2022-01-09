package by.alexandr7035.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 2)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun getDao(): NotesDao
}