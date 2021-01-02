package com.greater.leaguedex.ui.main.actions

import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.greater.leaguedex.util.ActivityAction

interface SnackBarManager : ActivityAction {
    fun showSnackBar(text: String, duration: Int = BaseTransientBottomBar.LENGTH_LONG)
    fun showSnackBar(@StringRes textRes: Int, duration: Int = BaseTransientBottomBar.LENGTH_LONG)
}
