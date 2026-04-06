package com.mwshubham.notes.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mwshubham.notes.data.local.NotesDatabase
import com.mwshubham.notes.data.local.getDatabaseBuilder
import com.mwshubham.notes.data.preferences.SettingsRepository
import com.mwshubham.notes.data.preferences.SettingsRepositoryImpl
import com.mwshubham.notes.data.preferences.createDataStore
import com.mwshubham.notes.data.repository.NoteRepositoryImpl
import com.mwshubham.notes.domain.repository.NoteRepository
import com.mwshubham.notes.domain.usecase.DeleteNoteUseCase
import com.mwshubham.notes.domain.usecase.GetAllNotesUseCase
import com.mwshubham.notes.domain.usecase.GetNoteByIdUseCase
import com.mwshubham.notes.domain.usecase.UpsertNoteUseCase
import com.mwshubham.notes.presentation.notedetail.NoteDetailViewModel
import com.mwshubham.notes.presentation.notelist.NoteListViewModel
import com.mwshubham.notes.presentation.settings.SettingsViewModel
import com.mwshubham.notes.presentation.splash.SplashViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val databaseModule = module {
    single<NotesDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<NotesDatabase>().noteDao() }
}

val preferencesModule = module {
    single { createDataStore() }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}

val repositoryModule = module {
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { GetAllNotesUseCase(get()) }
    factory { GetNoteByIdUseCase(get()) }
    factory { UpsertNoteUseCase(get()) }
    factory { DeleteNoteUseCase(get()) }
}

val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::NoteListViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { params -> NoteDetailViewModel(params.getOrNull(), get(), get(), get()) }
}

/** All Koin modules combined for a single startKoin call. */
val appModules = listOf(databaseModule, preferencesModule, repositoryModule, useCaseModule, viewModelModule)
