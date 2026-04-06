package com.mwshubham.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mwshubham.notes.data.local.initDatabaseContext
import com.mwshubham.notes.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Provide Android context to the Room database factory before Koin starts
        initDatabaseContext(this)

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