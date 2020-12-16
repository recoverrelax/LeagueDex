package com.greater.leaguedex.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseFragment<M : BaseViewModel<S>, S : BaseViewStates>(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {
    abstract val viewModel: M

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { event: S ->
                Timber.i("Received Event: $event")
                render(event)
            }
        }
    }

    override fun onDestroyView() {
        viewModel.viewScope.coroutineContext.cancelChildren()
        super.onDestroyView()
    }

    abstract fun render(viewState: S)
}
