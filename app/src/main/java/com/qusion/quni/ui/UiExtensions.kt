package com.qusion.quni.ui

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.showErrorSnackbar(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .show()
}