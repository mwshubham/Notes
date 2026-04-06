package com.mwshubham.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mwshubham.notes.core.lock.AppLockController
import com.mwshubham.notes.navigation.HelloWorld
import com.mwshubham.notes.navigation.NoteDetail
import com.mwshubham.notes.navigation.NoteList
import com.mwshubham.notes.navigation.Settings
import com.mwshubham.notes.navigation.Splash
import com.mwshubham.notes.presentation.helloworld.HelloWorldScreen
import com.mwshubham.notes.presentation.notedetail.NoteDetailScreen
import com.mwshubham.notes.presentation.notedetail.NoteDetailViewModel
import com.mwshubham.notes.presentation.notelist.NoteListScreen
import com.mwshubham.notes.presentation.notelist.NoteListViewModel
import com.mwshubham.notes.presentation.settings.SettingsScreen
import com.mwshubham.notes.presentation.settings.SettingsViewModel
import com.mwshubham.notes.presentation.splash.SplashScreen
import com.mwshubham.notes.presentation.splash.SplashViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.navigation3.runtime.NavKey as RuntimeNavKey

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
        // rememberNavBackStack serializes keys via the runtime NavKey base type.
        polymorphic(RuntimeNavKey::class) {
            subclass(Splash::class)
            subclass(HelloWorld::class)
            subclass(NoteList::class)
            subclass(NoteDetail::class)
            subclass(Settings::class)
        }
    }

    val configuration = SavedStateConfiguration {
        this.serializersModule = serializersModule
    }

    val backStack = rememberNavBackStack(
        configuration,
        Splash
    )

    // When AppLockController fires after the 30s grace period, reset navigation to Splash
    val isLocked by AppLockController.isLocked.collectAsState()
    LaunchedEffect(isLocked) {
        if (isLocked) {
            backStack.clear()
            backStack.add(Splash)
            AppLockController.reset()
        }
    }

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
                    // A unique key per navigation session ensures koinViewModel always
                    // creates a fresh instance rather than reusing a cached one.
                    val vmKey = remember { "NoteDetail_${key.id}_${System.currentTimeMillis()}" }
                    val vm: NoteDetailViewModel = koinViewModel(
                        key = vmKey,
                        parameters = { parametersOf(key.id) }
                    )
                    NoteDetailScreen(
                        viewModel = vm,
                        onNavigateBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
                    )
                }

                is Settings -> NavEntry(key) {
                    val vm: SettingsViewModel = koinViewModel()
                    SettingsScreen(
                        viewModel = vm,
                        onNavigateBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
                    )
                }

                else -> NavEntry(key) { /* no-op fallback */ }
            }
        }
    )
}
