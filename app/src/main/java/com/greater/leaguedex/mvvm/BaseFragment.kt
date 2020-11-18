package com.greater.leaguedex.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class BaseFragment<M : BaseViewModel<S>, S : BaseViewStates>(
    @LayoutRes
    private val contentRes: Int
) : Fragment(contentRes) {
    abstract val viewModel: M

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.viewState
                .onEach { event: S -> render(event) }
                .launchIn(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    abstract fun render(viewState: S)
}