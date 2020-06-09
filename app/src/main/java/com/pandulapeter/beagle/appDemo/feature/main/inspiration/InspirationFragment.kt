package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.databinding.FragmentInspirationBinding
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import com.pandulapeter.beagle.appDemo.utils.showToast
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class InspirationFragment : BaseViewModelFragment<FragmentInspirationBinding, InspirationViewModel>(R.layout.fragment_inspiration, true, R.string.inspiration_title) {

    override val viewModel by viewModel<InspirationViewModel>()
    private val tutorialSwitch by lazy {
        SwitchModule(
            id = TUTORIAL_SWITCH_ID, // Persisting modules only works if we have a constant ID.
            initialValue = true,
            shouldBePersisted = true,
            text = getString(R.string.inspiration_beagle_tutorial_switch),
            onValueChanged = ::onShouldShowTutorialToggled // Persisted modules invoke their callback when the Activity starts so we can rely on this to initialize the list.
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBeagle(tutorialSwitch.getCurrentValue(Beagle) == true)
    }

    private fun setupRecyclerView() {
        val inspirationAdapter = InspirationAdapter(::onCaseStudySelected)
        binding.recyclerView.run {
            adapter = inspirationAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        viewModel.items.observeForever(inspirationAdapter::submitList)
    }

    private fun onShouldShowTutorialToggled(shouldShowTutorial: Boolean) {
        viewModel.refreshItems(shouldShowTutorial)
        setupBeagle(shouldShowTutorial)
    }

    private fun setupBeagle(shouldShowTutorial: Boolean) {
        Beagle.setModules(
            *(mutableListOf<Module<*>>().apply {
                add(
                    TextModule(
                        id = TUTORIAL_TEXT_ID, // This module has a constant ID to avoid glitches when refreshing the list.
                        text = getText(R.string.inspiration_beagle_text_1)
                    )
                )
                if (shouldShowTutorial) {
                    add(LabelModule(title = getString(R.string.inspiration_beagle_label)))
                    add(SwitchModule(text = getString(R.string.inspiration_beagle_switch), onValueChanged = {
                        showToast(if (it) R.string.inspiration_beagle_switch_toast_on else R.string.inspiration_beagle_switch_toast_off)
                    }))
                    add(ButtonModule(text = getString(R.string.inspiration_beagle_button), onButtonPressed = {
                        showToast(R.string.inspiration_beagle_button_toast)
                    }))
                    add(CheckBoxModule(text = getString(R.string.inspiration_beagle_check_box), onValueChanged = {
                        showToast(if (it) R.string.inspiration_beagle_check_box_toast_on else R.string.inspiration_beagle_check_box_toast_off)
                    }))
                    add(listOf(
                        RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_1)),
                        RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_2)),
                        RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_3))
                    ).let { radioGroupOptions ->
                        SingleSelectionListModule(
                            title = getString(R.string.inspiration_beagle_radio_group),
                            items = radioGroupOptions,
                            initiallySelectedItemId = radioGroupOptions.first().id,
                            onSelectionChanged = { showToast(it?.name.orEmpty()) }
                        )
                    })
                    add(TextModule(text = getText(R.string.inspiration_beagle_text_2)))
                }
                add(tutorialSwitch)
            }.toTypedArray())
        )
    }

    private fun onCaseStudySelected(caseStudy: CaseStudy) = showToast(caseStudy.title)

    private data class RadioGroupOption(
        override val name: String,
        override val id: String = name
    ) : BeagleListItemContract

    companion object {
        private const val TUTORIAL_TEXT_ID = "tutorialText"
        private const val TUTORIAL_SWITCH_ID = "tutorialSwitch"

        fun newInstance() = InspirationFragment()
    }
}