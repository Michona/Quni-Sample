package com.qusion.quni.ui

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showErrorSnackbar(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .show()
}