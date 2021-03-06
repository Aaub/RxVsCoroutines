package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.ui.MainViewModel.STATUS.LOADING
import com.alexetnico.rxvscoroutines.ui.MainViewModel.STATUS.NOT_LOADING
import com.alexetnico.rxvscoroutines.ui.customview.BeerView
import com.alexetnico.rxvscoroutines.usecase.BeerUseCase
import com.alexetnico.rxvscoroutines.utils.BaseViewModel
import com.alexetnico.rxvscoroutines.utils.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch


class MainViewModel(key: String) : BaseViewModel() {


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

    private fun randomBeerCo() = launch {
        _beerCo.postValue(BeerView.Model(isLoading = true))
        _beerCo.postValue(beerUseCase.randomBeerCo().toBeerViewModel())
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

    private fun beerWithImageCo() = launch {
        _beerImageCo.postValue(BeerView.Model(isLoading = true))
        _beerImageCo.postValue(beerUseCase.beerWithImageCo().toBeerViewModel())
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
        launch {
            beerUseCase.randomBeersCo(quantity).consumeEach {
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
        .doOnSubscribe {
            _beersRx.postValue(emptyList())
            _beersStatusRx.postValue(LOADING)
        }
        .doFinally { _beersStatusRx.postValue(NOT_LOADING) }
        .subscribeBy(
            onNext = { _beersRx.postValue(_beersRx.value?.plus(it.name)) },
            onComplete = { },
            onError = { }
        )


    /***********  RECURSIVE  ***********/

    private fun beerWithSafeImageCo() = launch {
        _beerImageCo.postValue(BeerView.Model(isLoading = true))
        _beerImageCo.postValue(beerUseCase.beerWithSafeImageCo().toBeerViewModel())
    }

    private fun beerWithSafeImageRx() = beerUseCase
        .beerWithSafeImageRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _beerImageRx.postValue(BeerView.Model(isLoading = true)) }
        .subscribeBy(
            onSuccess = { _beerImageRx.postValue(it.toBeerViewModel()) },
            onError = { }
        )

    private fun Beer.toBeerViewModel() = BeerView.Model(
        name = name,
        abv = abv ?: "??",
        image_url = image?.url
    )

}