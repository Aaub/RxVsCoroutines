package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import com.alexetnico.rxvscoroutines.usecase.BeerUseCase
import com.alexetnico.rxvscoroutines.utils.Beers
import com.alexetnico.rxvscoroutines.utils.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class MainViewModel(private val key: String) : ViewModel() {
    private val beerUseCase = BeerUseCase(key)
    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }

    private val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    private val _beersCo = MutableLiveData<Beers>()
    val beersCo: LiveData<Beers> = _beersCo

    private val _beersRx = MutableLiveData<Beers>()
    val beersRx: LiveData<Beers> = _beersRx

    private val _beerRx = MutableLiveData<Beer>()
    val beerRx: LiveData<Beer> = _beerRx

    init {
        randomBeerRx()
    }

    private fun getBeersCo() {
        val breweryResult = runBlocking(coroutineContext, block = {
            async {
                coroutinesService.beers(key).await()
            }.await()
        })

        _beersCo.postValue(breweryResult.beers)
    }

    private fun getBeersRx() = beerUseCase
        .beers()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribeBy(
            onSuccess = { _beersRx.postValue(it) },
            onError = { Log.e("MainViewModel", it.message) }
        )

    private fun randomBeerRx() = beerUseCase
        .randomBeer()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribeBy(
            onSuccess = { _beerRx.postValue(it) },
            onError = { Log.e("MainViewModel", it.message) }
        )

}