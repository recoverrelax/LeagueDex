package com.greater.leaguedex.util

import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

inline fun <reified A : ActivityAction> Fragment.activityAction(action: A.() -> Unit) {
    this.activity?.let {
        if (activity !is A) {
            throw IllegalStateException("Activity must implement: ${A::class.java}")
        }
        return action(it as A)
    } ?: throw IllegalStateException("Cannot perform action on null activity!")
}
