package com.pandulapeter.debugMenuExample

import android.app.Application
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration
import com.pandulapeter.debugMenuCore.modules.HeaderModule
import com.pandulapeter.debugMenuCore.modules.SettingsLinkModule

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugMenu.initialize(
            application = this,
            configuration = DebugMenuConfiguration(
                headerModule = HeaderModule(
                    title = getString(R.string.app_name),
                    subtitle = "v${BuildConfig.VERSION_NAME}"
                ),
                settingsLinkModule = SettingsLinkModule()
            )
        )
    }
}