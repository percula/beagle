package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class PlaygroundAdapter(
    scope: CoroutineScope,
    private val onButtonClicked: (ButtonViewHolder.UiModel) -> Unit
) : BaseAdapter<PlaygroundListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ModuleViewHolder.UiModel -> R.layout.item_playground_module
        is ButtonViewHolder.UiModel -> R.layout.item_playground_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_playground_module -> ModuleViewHolder.create(parent)
        R.layout.item_playground_button -> ButtonViewHolder.create(parent, onButtonClicked)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is ModuleViewHolder -> holder.bind(getItem(position) as ModuleViewHolder.UiModel)
        is ButtonViewHolder -> holder.bind(getItem(position) as ButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}