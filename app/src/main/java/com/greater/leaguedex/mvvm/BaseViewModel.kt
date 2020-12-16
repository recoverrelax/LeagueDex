package com.greater.leaguedex.mvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel<S : BaseViewStates> : ViewModel() {
    private val _viewState = MutableSharedFlow<S>()
    val viewState: Flow<S> = _viewState.asSharedFlow()
    val viewScope = CoroutineScope(Dispatchers.Main.immediate + Job())

    protected fun postEvent(event: S) {
        Timber.i("Posted event: $event")
        viewScope.launch { _viewState.emit(event) }
    }

    override fun onCleared() {
        viewScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}
