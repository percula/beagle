package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemRadioButtonCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.list.delegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.SingleSelectionListModule

internal class SingleSelectionListDelegate<T : BeagleListItemContract> : ExpandableModuleDelegate<SingleSelectionListModule<T>>, PersistableModuleDelegate.String<SingleSelectionListModule<T>>() {

    override fun canExpand(module: SingleSelectionListModule<T>) = module.items.isNotEmpty()

    override fun getTitle(module: SingleSelectionListModule<T>) = if (module.shouldRequireConfirmation && hasPendingChanges(module)) module.title.append("*") else module.title

    override fun MutableList<Cell<*>>.addItems(module: SingleSelectionListModule<T>) {
        addAll(module.items.map { item ->
            ExpandedItemRadioButtonCell(
                id = "${module.id}_${item.id}",
                text = item.title,
                isChecked = item.id == getUiValue(module),
                isEnabled = module.isEnabled,
                onValueChanged = { setUiValue(module, item.id) }
            )
        })
    }

    override fun createCells(module: SingleSelectionListModule<T>) = super.createCells(module).also {
        callListenerForTheFirstTimeIfNeeded(module, getCurrentValue(module))
    }
}