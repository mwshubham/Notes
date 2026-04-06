package com.mwshubham.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ProcessLifecycleOwner
import com.mwshubham.notes.core.lock.AppLifecycleObserver
import com.mwshubham.notes.data.local.initDatabaseContext
import com.mwshubham.notes.data.preferences.initDataStoreContext
import com.mwshubham.notes.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Provide Android context to the Room database factory before Koin starts
        initDatabaseContext(this)
        // Provide Android context to the DataStore preferences factory
        initDataStoreContext(this)

        // Observe whole-process lifecycle for background lock
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())

        // Start Koin with Android context so koin-android features work (e.g. androidContext())
        startKoin {
            androidContext(this@MainActivity)
            modules(appModules)
        }

        setContent {
            App()
        }
    }
}