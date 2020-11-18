package com.greater.leaguedex.mvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber

open class BaseViewModel<S: BaseViewStates>: ViewModel() {
    private val _viewState = BroadcastChannel<S>(capacity = Channel.BUFFERED)
    val viewState: Flow<S> = _viewState.asFlow()

    protected fun postEvent(event: S) {
        Timber.i("Posted event: $event")
        _viewState.offer(event)
    }
}