package com.alexetnico.rxvscoroutines.utils

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {
    final override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val coroutineScope = coroutineContext + Job()

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}