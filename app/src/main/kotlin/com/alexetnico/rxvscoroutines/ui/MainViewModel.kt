package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.ui.MainViewModel.STATUS.LOADING
import com.alexetnico.rxvscoroutines.ui.MainViewModel.STATUS.NOT_LOADING
import com.alexetnico.rxvscoroutines.ui.customview.BeerView
import com.alexetnico.rxvscoroutines.usecase.BeerUseCase
import com.alexetnico.rxvscoroutines.utils.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach


class MainViewModel(key: String) : ViewModel() {
    private val beerUseCase = BeerUseCase(key)

    enum class STATUS { LOADING, NOT_LOADING }

    private val _beerCo = MutableLiveData<BeerView.Model>()
    val beerCo: LiveData<BeerView.Model> = _beerCo

    private val _beerImageCo = MutableLiveData<BeerView.Model>()
    val beerImageCo: LiveData<BeerView.Model> = _beerImageCo

    private val _beersCo = MutableLiveData<Collection<String>>()
    val beersCo: LiveData<Collection<String>> = _beersCo

    private val _beersStatusCo = MutableLiveData<STATUS>()
    val beersStatusCo: LiveData<STATUS> = _beersStatusCo

    private val _beerRx = MutableLiveData<BeerView.Model>()
    val beerRx: LiveData<BeerView.Model> = _beerRx

    private val _beerImageRx = MutableLiveData<BeerView.Model>()
    val beerImageRx: LiveData<BeerView.Model> = _beerImageRx

    private val _beersRx = MutableLiveData<Collection<String>>()
    val beersRx: LiveData<Collection<String>> = _beersRx

    private val _beersStatusRx = MutableLiveData<STATUS>()
    val beersStatusRx: LiveData<STATUS> = _beersStatusRx


    fun fetchRandomBeer() {
        randomBeerCo()
        randomBeerRx()
    }

    fun fetchBeerImage() {
        beerWithImageCo()
        beerWithImageRx()
    }

    fun fetchBeerSafeImage() {
        beerWithSafeImageRx()
        beerWithSafeImageCo()
    }

    fun fetchRandomBeers(quantity: Int) {
        randomBeersRx(quantity)
        randomBeersCo(quantity)
    }


    /*********** Random ***********/

    private fun randomBeerCo() {

        GlobalScope.async(Dispatchers.Default) {
            _beerCo.postValue(BeerView.Model(isLoading = true))
            _beerCo.postValue(beerUseCase.randomBeerCo().toBeerViewModel())
        }
    }

    private fun randomBeerRx() = beerUseCase
        .randomBeerRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _beerRx.postValue(BeerView.Model(isLoading = true)) }
        .subscribeBy(
            onSuccess = { _beerRx.postValue(it.toBeerViewModel()) },
            onError = { }
        )


    /*********** Beer with image ***********/

    private fun beerWithImageCo() {
        GlobalScope.async(Dispatchers.Default) {
            _beerImageCo.postValue(BeerView.Model(isLoading = true))
            _beerImageCo.postValue(beerUseCase.beerWithImageCo().toBeerViewModel())
        }
    }

    private fun beerWithImageRx() = beerUseCase
        .beerWithImageRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _beerImageRx.postValue(BeerView.Model(isLoading = true)) }
        .subscribeBy(
            onSuccess = { _beerImageRx.postValue(it.toBeerViewModel()) },
            onError = { }
        )


    /*********** Calls in raw ***********/

    private fun randomBeersCo(quantity: Int) {
        _beersStatusCo.postValue(LOADING)
        _beersCo.postValue(emptyList())
        beerUseCase.randomBeersCo(quantity)
        GlobalScope.async(Dispatchers.Default) {
            beerUseCase.channel.consumeEach {
                it?.let {
                    _beersCo.postValue(_beersCo.value?.plus(it.name))
                }
            }
            _beersStatusCo.postValue(NOT_LOADING)
        }
    }

    private fun randomBeersRx(quantity: Int) = beerUseCase
        .randomBeersRx(quantity.toLong())
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribeBy(
            onNext = { _beersRx.postValue(_beersRx.value?.plus(it.name)) },
            onComplete = { },
            onError = { }
        )


    /***********  RECURSIVE  ***********/

    private fun beerWithSafeImageRx() = beerUseCase
        .beerWithSafeImageRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _beerImageRx.postValue(BeerView.Model(isLoading = true)) }
        .subscribeBy(
            onSuccess = { _beerImageRx.postValue(it.toBeerViewModel()) },
            onError = { }
        )

    private fun beerWithSafeImageCo() = GlobalScope.async {
        _beerImageCo.postValue(BeerView.Model(isLoading = true))
        _beerImageCo.postValue(beerUseCase.beerWithSafeImageCo().toBeerViewModel())
    }


    private fun Beer.toBeerViewModel() = BeerView.Model(
        name = name,
        abv = abv ?: "??",
        image_url = image?.url
    )

}