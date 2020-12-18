package com.greater.leaguedex.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun Lifecycle.observe(
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    this.addObserver(
        object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                onStart()
            }

            override fun onStop(owner: LifecycleOwner) {
                onStop()
                removeObserver(this)
            }
        }
    )
}
