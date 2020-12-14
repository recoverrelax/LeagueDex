package com.greater.leaguedex.mvvm

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseActivity<M : BaseViewModel<S>, S : BaseViewStates> : AppCompatActivity() {
    abstract val viewModel: M

    private val lifeCycleScope = CoroutineScope(Dispatchers.Main.immediate + Job())

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
        viewModel.viewState.onEach { event: S -> render(event) }.launchIn(lifeCycleScope)
    }

    override fun onDestroy() {
        lifeCycleScope.coroutineContext.cancelChildren()
        super.onDestroy()
    }

    data class FragAnimations(
        val enter: Int,
        val exit: Int,
        val popEnter: Int,
        val popExit: Int
    ) {
        companion object {
            val NONE = FragAnimations(enter = 0, exit = 0, popEnter = 0, popExit = 0)
            /*val SLIDE = FragAnimations(
                enter = R.anim.slide_left_in,
                exit = R.anim.slide_left_out,
                popEnter = R.anim.slide_right_in,
                popExit = R.anim.slide_right_out
            )*/
        }
    }
}
