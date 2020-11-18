package com.greater.leaguedex.util

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityBindingDelegate<T : ViewBinding>(
    activity: ComponentActivity,
    private val viewBindingFactory: (LayoutInflater) -> T
) : ReadOnlyProperty<ComponentActivity, T> {
    private var binding: T? = null

    init {
        activity.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    super.onCreate(owner)
                    binding = viewBindingFactory(activity.layoutInflater)
                        .also { activity.setContentView(it.root) }
                }
            }
        )
    }

    override fun getValue(thisRef: ComponentActivity, property: KProperty<*>): T {
        return this.binding
            ?: throw IllegalStateException("Should not attempt to get bindings after Activity onDestroy")
    }
}

fun <T : ViewBinding> ComponentActivity.viewBinding(viewBindingFactory: (LayoutInflater) -> T) =
    ActivityBindingDelegate(this, viewBindingFactory)
