package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alexetnico.rxvscoroutines.model.Beer
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import com.alexetnico.rxvscoroutines.repo.coroutines.BreweryApiServiceFactory as CoroutinesApi

class MainViewModel : ViewModel() {
    private val _beers = MutableLiveData<List<Beer>>()
    val beers: LiveData<List<Beer>> = _beers

    private val breweryApiServiceFactory by lazy {
        CoroutinesApi.createService()
    }

    private val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    init {
        getBeers()
    }

    private fun getBeers() {
        val breweryResult = runBlocking(coroutineContext, block = {
            async {
                breweryApiServiceFactory.beers("3bf68d8eb96cbe15ce31cfd493d981c1").await()
            }.await()
        })

        _beers.postValue(breweryResult.data)
    }

}