package by.alexandr7035.txnotes.di

import by.alexandr7035.data.repository.NotesRepositoryImpl
import by.alexandr7035.domain.repository.NotesRepository
import by.alexandr7035.domain.usecase.GetNotesListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNotesRepository(): NotesRepository {
        return NotesRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGetNotesListUseCase(notesRepository: NotesRepository): GetNotesListUseCase {
        return GetNotesListUseCase(notesRepository)
    }
}