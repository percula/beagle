package com.pandulapeter.beagle.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.core.view.OverlayFrameLayout
import kotlin.math.min
import kotlin.math.roundToInt


@SuppressLint("ViewConstructor")
internal class DebugMenuDrawerLayout(
    context: Context,
    val drawer: View
) : DrawerLayout(context) {

    private val listener = object : DrawerListener {

        override fun onDrawerOpened(drawerView: View) = Unit

        override fun onDrawerClosed(drawerView: View) = BeagleCore.implementation.notifyVisibilityListenersOnHide()

        override fun onDrawerStateChanged(newState: Int) {
            if (newState == STATE_DRAGGING && !isDrawerOpen(drawer)) {
                BeagleCore.implementation.notifyVisibilityListenersOnShow()
            }
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
    }

    init {
        addView(OverlayFrameLayout(context), LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        addView(drawer, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, GravityCompat.END))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent) = if (ev.action == MotionEvent.ACTION_DOWN) {
        if (isDrawerVisible(drawer)) {
            super.onTouchEvent(ev)
        } else (((width - 2 * ViewConfiguration.get(context).scaledTouchSlop) <= ev.x) && super.onTouchEvent(ev))
    } else super.onTouchEvent(ev)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setDrawerLockMode(if (Beagle.isUiEnabled) LOCK_MODE_UNDEFINED else LOCK_MODE_LOCKED_CLOSED)
        addDrawerListener(listener)
        drawer.run {
            val displayMetrics = DisplayMetrics()
            BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            layoutParams = layoutParams.apply {
                width = min(resources.getDimensionPixelSize(R.dimen.beagle_drawer_maximum_width), (displayMetrics.widthPixels * DRAWER_WIDTH_RATIO).roundToInt())
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeDrawerListener(listener)
    }

    companion object {
        private const val DRAWER_WIDTH_RATIO = 0.6f
    }
}