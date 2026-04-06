package com.mwshubham.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.mwshubham.notes.navigation.HelloWorld
import com.mwshubham.notes.navigation.NavKey
import com.mwshubham.notes.navigation.NoteDetail
import com.mwshubham.notes.navigation.NoteList
import com.mwshubham.notes.navigation.Splash
import com.mwshubham.notes.presentation.helloworld.HelloWorldScreen
import com.mwshubham.notes.presentation.notedetail.NoteDetailScreen
import com.mwshubham.notes.presentation.notedetail.NoteDetailViewModel
import com.mwshubham.notes.presentation.notelist.NoteListScreen
import com.mwshubham.notes.presentation.notelist.NoteListViewModel
import com.mwshubham.notes.presentation.splash.SplashScreen
import com.mwshubham.notes.presentation.splash.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * iOS actual — simple state-driven navigation using a SnapshotStateList as backstack.
 * Nav3 NavDisplay is Android-only; on iOS we drive a `when` on the top of the stack.
 */
@Composable
actual fun App() {
    val backStack = remember { mutableStateListOf<NavKey>(Splash) }
    val current = backStack.lastOrNull() ?: Splash

    when (val key = current) {
        is Splash -> {
            val vm: SplashViewModel = koinViewModel()
            SplashScreen(
                viewModel = vm,
                onNavigate = { destination ->
                    backStack.clear()
                    backStack.add(destination)
                }
            )
        }

        is HelloWorld -> HelloWorldScreen()

        is NoteList -> {
            val vm: NoteListViewModel = koinViewModel()
            NoteListScreen(
                viewModel = vm,
                onNavigate = { destination -> backStack.add(destination) }
            )
        }

        is NoteDetail -> {
            val vm: NoteDetailViewModel = koinViewModel(
                parameters = { parametersOf(key.id) }
            )
            NoteDetailScreen(
                viewModel = vm,
                onNavigateBack = { if (backStack.size > 1) backStack.removeLast() }
            )
        }
    }
}
