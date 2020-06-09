package com.pandulapeter.beagle.appDemo

import android.app.Application
import com.pandulapeter.beagle.Beagle
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class BeagleDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BeagleDemoApplication)
            modules(modules)
        }
        Beagle.initialize(this)
    }
}