package by.alexandr7035.txnotes.core

import android.app.Application
import by.alexandr7035.txnotes.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // Setup timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}