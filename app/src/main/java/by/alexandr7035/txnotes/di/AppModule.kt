package by.alexandr7035.txnotes.di

import android.content.Context
import androidx.room.Room
import by.alexandr7035.data.local.NotesDao
import by.alexandr7035.data.local.NotesDatabase
import by.alexandr7035.data.repository.NotesRepositoryImpl
import by.alexandr7035.domain.repository.NotesRepository
import by.alexandr7035.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): NotesDatabase {
        return Room
            .databaseBuilder(context, NotesDatabase::class.java, "notes.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNotesDao(database: NotesDatabase): NotesDao {
        return database.getDao()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(notesDao: NotesDao): NotesRepository {
        return NotesRepositoryImpl(notesDao)
    }


    // FIXME scope to viewmodel
    @Provides
    @Singleton
    fun provideGetNotesListUseCase(notesRepository: NotesRepository): GetNotesListUseCase {
        return GetNotesListUseCase(notesRepository)
    }

    @Provides
    @Singleton
    fun provideGetNoteByIdUseCase(notesRepository: NotesRepository): GetNoteByIdUseCase {
        return GetNoteByIdUseCase(notesRepository)
    }

    @Provides
    @Singleton
    fun provideCreateNoteUseCase(notesRepository: NotesRepository): CreateNoteUseCase {
        return CreateNoteUseCase(notesRepository)
    }

    @Provides
    @Singleton
    fun provideEditNoteUseCase(notesRepository: NotesRepository): EditNoteUseCase {
        return EditNoteUseCase(notesRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteNotesUseCase(notesRepository: NotesRepository): DeleteNotesUseCase {
        return DeleteNotesUseCase(notesRepository)
    }
}