package com.mwshubham.notes

import androidx.compose.runtime.Composable
import androidx.navigation3.SavedStateConfiguration
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
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
<<<<<<< HEAD
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Android actual — Nav3 [NavDisplay] with a state-driven [rememberNavBackStack].
 *
 * Theme switching per destination:
 * - Splash / HelloWorld → plain/bright (disguise)
 * - NoteList / NoteDetail → VaultTheme (dark, premium)
 */
@Composable
actual fun App() {
    val serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Splash::class)
            subclass(HelloWorld::class)
            subclass(NoteList::class)
            subclass(NoteDetail::class)
        }
    }

    val configuration = SavedStateConfiguration(
        serializersModule = serializersModule
    )

    val backStack = rememberNavBackStack(
        configuration,
        Splash
    )

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
        entryProvider = { key ->
            when (key) {
                is Splash -> NavEntry(key) {
                    val vm: SplashViewModel = koinViewModel()
                    SplashScreen(
                        viewModel = vm,
                        onNavigate = { destination ->
                            backStack.clear()
                            backStack.add(destination)
                        }
                    )
                }

                is HelloWorld -> NavEntry(key) {
                    HelloWorldScreen()
                }

                is NoteList -> NavEntry(key) {
                    val vm: NoteListViewModel = koinViewModel()
                    NoteListScreen(
                        viewModel = vm,
                        onNavigate = { destination -> backStack.add(destination) }
                    )
                }

                is NoteDetail -> NavEntry(key) {
                    val vm: NoteDetailViewModel = koinViewModel(
                        parameters = { parametersOf(key.id) }
                    )
                    NoteDetailScreen(
                        viewModel = vm,
                        onNavigateBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
                    )
                }

                else -> NavEntry(key) { /* no-op fallback */ }
            }
        }
    )
}
