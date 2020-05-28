package com.pandulapeter.beagle.core.util.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

internal fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}