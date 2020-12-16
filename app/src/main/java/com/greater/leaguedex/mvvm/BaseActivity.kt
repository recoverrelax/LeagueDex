package com.greater.leaguedex.mvvm

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseActivity<M : BaseViewModel<S>, S : BaseViewStates> : AppCompatActivity() {
    abstract val viewModel: M

    protected fun replaceFragment(
        @IdRes containerId: Int,
        ignoreStateLoss: Boolean = false,
        animations: FragAnimations = FragAnimations.NONE,
        fragmentSupplier: () -> androidx.fragment.app.Fragment,
        addToBackStack: Boolean = true
    ) {
        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(
                animations.enter,
                animations.exit,
                animations.popEnter,
                animations.popExit
            )

            if (addToBackStack) {
                addToBackStack(null)
            }
            val frag = fragmentSupplier.invoke()
            replace(containerId, frag, frag::class.toString())

            if (ignoreStateLoss) {
                commitAllowingStateLoss()
            } else {
                commit()
            }
        }
    }

    abstract fun render(viewState: S)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.onEach { event: S -> render(event) }.launchIn(lifecycleScope)
    }

    data class FragAnimations(
        val enter: Int,
        val exit: Int,
        val popEnter: Int,
        val popExit: Int
    ) {
        companion object {
            val NONE = FragAnimations(enter = 0, exit = 0, popEnter = 0, popExit = 0)
        }
    }
}
