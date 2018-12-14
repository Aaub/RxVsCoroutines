package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class MainViewModel(private val key: String) : ViewModel() {
    private val coroutinesService by lazy {
        BreweryApiServiceFactory.createCoroutinesService()
    }

    private val rxService by lazy {
        BreweryApiServiceFactory.createRxService()
    }

    private val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    private val _beers = MutableLiveData<List<Beer>>()
    val beers: LiveData<List<Beer>> = _beers

    init {
        getBeers()
    }

    private fun getBeers() {
        val breweryResult = runBlocking(coroutineContext, block = {
            async {
                coroutinesService.beers(key).await()
            }.await()
        })

        _beers.postValue(breweryResult.data)
    }

}