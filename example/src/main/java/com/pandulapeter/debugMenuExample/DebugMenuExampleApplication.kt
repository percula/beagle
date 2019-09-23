package com.pandulapeter.debugMenuExample

import android.app.Application
import android.widget.Toast
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.configuration.modules.AppInfoButtonModule
import com.pandulapeter.debugMenuCore.configuration.modules.ButtonModule
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayToggleModule
import com.pandulapeter.debugMenuCore.configuration.modules.LogListModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLogListModule
import com.pandulapeter.debugMenuCore.configuration.modules.TextModule
import com.pandulapeter.debugMenuCore.configuration.modules.LongTextModule
import com.pandulapeter.debugMenuCore.configuration.modules.SingleSelectionListModule
import com.pandulapeter.debugMenuCore.configuration.modules.ToggleModule
import com.pandulapeter.debugMenuExample.networking.NetworkingManager
import com.pandulapeter.debugMenuExample.utils.mockBackendEnvironments

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            DebugMenu.attachToApplication(this)
            DebugMenu.setModules(
                listOf(
                    HeaderModule(
                        title = getString(R.string.app_name),
                        subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        text = "Built on ${BuildConfig.BUILD_DATE}"
                    ),
                    AppInfoButtonModule(),
                    TextModule(
                        text = "Random text 1"
                    ),
                    KeylineOverlayToggleModule(),
                    ToggleModule(
                        title = "Feature toggle 1",
                        onValueChanged = { isOn -> "Feature 1 is ${if (isOn) "on" else "off"}".showToast() }
                    ),
                    ToggleModule(
                        title = "Feature toggle 2",
                        onValueChanged = { isOn -> "Feature 2 is ${if (isOn) "on" else "off"}".showToast() }
                    ),
                    NetworkLogListModule(
                        baseUrl = NetworkingManager.BASE_URL,
                        shouldShowHeaders = true,
                        shouldShowTimestamp = true
                    ),
                    LogListModule(shouldShowTimestamp = true),
                    SingleSelectionListModule(
                        title = "Environment",
                        items = mockBackendEnvironments,
                        isInitiallyExpanded = true,
                        initialSelectionId = "Develop",
                        onItemSelectionChanged = { backendEnvironment -> backendEnvironment.url.showToast() }
                    ),
                    TextModule(
                        text = "Random text 2"
                    ),
                    LongTextModule(
                        title = "Long text",
                        text = "Here is a longer piece of text that occupies more space so it doesn't make sense to always have it fully displayed."
                    ),
                    ButtonModule(
                        text = "Show a toast",
                        onButtonPressed = { "Here is a toast".showToast() }
                    )
                )
            )
        }
    }

    private fun String.showToast() = Toast.makeText(this@DebugMenuExampleApplication, this, Toast.LENGTH_SHORT).show()
}