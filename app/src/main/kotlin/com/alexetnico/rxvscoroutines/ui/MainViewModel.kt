package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import com.alexetnico.rxvscoroutines.utils.Beers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class MainViewModel(private val key: String) : ViewModel() {
    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    private val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    private val _beers = MutableLiveData<Beers>()
    private val _beersRx = MutableLiveData<Beers>()

    val beers: LiveData<Beers> = _beers
    val beersRx: LiveData<Beers> = _beersRx

    init {
        getBeersCo()
        getBeersRx()
    }


    private fun getBeersCo() {
        val breweryResult = runBlocking(coroutineContext, block = {
            async {
                coroutinesService.beers(key).await()
            }.await()
        })

        _beers.postValue(breweryResult.data)
    }

    private fun getBeersRx() = rxService
        .beers(key)
        .subscribeOn(Schedulers.io())
        .subscribeBy(
            onSuccess = { _beersRx.postValue(it.data) },
            onError = { Log.e("MainViewModel", it.message) }
        )

}