package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.builtIn.SwitchModuleContract
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import java.util.UUID

/**
 * Displays a simple switch.
 *
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [shouldBePersisted]]). Optional, random string by default.
 * @param text - The text to display on the switch.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param initialValue - Whether or not the switch is checked initially. Optional, false by default. If [shouldBePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param shouldBePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added.
 */
open class SwitchModule(
    final override val id: String = "switch_${UUID.randomUUID()}",
    override val text: CharSequence,
    @ColorInt override val color: Int? = null,
    final override val initialValue: Boolean = false,
    final override val shouldBePersisted: Boolean = false,
    final override val onValueChanged: (Boolean) -> Unit
) : SwitchModuleContract {

    final override var currentValue: Boolean = if (shouldBePersisted) {
        BeagleCore.implementation.sharedPreferencesManager.switchModules[id] ?: initialValue
    } else initialValue
        private set(value) {
            field = value
            onValueChanged(value)
            if (shouldBePersisted) {
                BeagleCore.implementation.sharedPreferencesManager.switchModules[id] = value
            }
        }

    init {
        if (shouldBePersisted) {
            onValueChanged(currentValue)
        }
    }

    final override fun createCells() = listOf<Cell<*>>(SwitchCell(id, text, color, currentValue) { currentValue = it })
}