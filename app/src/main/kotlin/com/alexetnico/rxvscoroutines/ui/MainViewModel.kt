package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.usecase.GetBeer
import com.alexetnico.rxvscoroutines.usecase.GetBeers
import com.alexetnico.rxvscoroutines.utils.Beers
import com.alexetnico.rxvscoroutines.utils.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val key: String) : ViewModel() {
    private val ucGetBeer = GetBeer(key)
    private val ucGetBeers = GetBeers(key)

    private val _beersCo = MutableLiveData<Beers>()
    val beersCo: LiveData<Beers> = _beersCo

    private val _beerCo = MutableLiveData<Beer>()
    val beerCo: LiveData<Beer> = _beerCo


    private val _beersRx = MutableLiveData<Beers>()
    val beersRx: LiveData<Beers> = _beersRx

    private val _beerRx = MutableLiveData<Beer>()
    val beerRx: LiveData<Beer> = _beerRx

    init {
        randomBeerRx()
        randomBeerCo()
    }


    private fun getBeersCo() {
        val test = ucGetBeers.beersCo()
        _beersCo.postValue(test)
    }


    private fun randomBeerCo() {
        _beerCo.postValue(ucGetBeer.randomBeerCo())
    }

    private fun getBeersRx() = ucGetBeers
        .beersRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribeBy(
            onSuccess = { _beersRx.postValue(it) },
            onError = { Log.e("MainViewModel", it.message) }
        )

    private fun randomBeerRx() = ucGetBeer
        .randomBeerRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribeBy(
            onSuccess = { _beerRx.postValue(it) },
            onError = { Log.e("MainViewModel", it.message) }
        )

}