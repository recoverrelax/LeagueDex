package com.greater.leaguedex.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseFragment<M : BaseViewModel<S>, S : BaseViewStates>(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {
    abstract val viewModel: M

    val lifeCycleScope = CoroutineScope(Dispatchers.Main.immediate + Job())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifeCycleScope.launch {
            viewModel.viewState.collect { event: S ->
                Timber.i("Received Event: $event")
                render(event)
            }
        }
    }

    override fun onDestroyView() {
        lifeCycleScope.coroutineContext.cancelChildren()
        super.onDestroyView()
    }

    abstract fun render(viewState: S)
}
