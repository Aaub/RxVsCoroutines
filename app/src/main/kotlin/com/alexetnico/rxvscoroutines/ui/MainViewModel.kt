package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.ui.customview.BeerView
import com.alexetnico.rxvscoroutines.usecase.BeerUseCase
import com.alexetnico.rxvscoroutines.utils.EMPTY
import com.alexetnico.rxvscoroutines.utils.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay

class MainViewModel(key: String) : ViewModel() {
    private val beerUseCase = BeerUseCase(key)

    private val _beerCo = MutableLiveData<BeerView.Model>()
    val beerCo: LiveData<BeerView.Model> = _beerCo

    private val _beerImageCo = MutableLiveData<String>()
    val beerImageCo: LiveData<String> = _beerImageCo

    private val _beerRx = MutableLiveData<BeerView.Model>()
    val beerRx: LiveData<BeerView.Model> = _beerRx


    fun fetchRandomBeer() {
//        randomBeerCo()
        fiveBeers()
        randomBeerRx()
//        beerWithImage()
    }

    /** Random **/

    private fun randomBeerCo() {

        GlobalScope.async(Dispatchers.Default) {
            _beerCo.postValue(beerUseCase.randomCo().await()?.toBeerViewModel())
        }


    }

    private fun randomBeerRx() = beerUseCase
        .randomRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _beerRx.postValue(BeerView.Model(isLoading = true)) }
        .subscribeBy(
            onSuccess = { _beerRx.postValue(it.toBeerViewModel()) },
            onError = { Log.e("MainViewModel", it.message) }
        )


    /** Beer with image **/

    private fun beerWithImage() {
        GlobalScope.async(Dispatchers.Default) {
            _beerCo.postValue(beerUseCase.beerWithImageCo().await()?.toBeerViewModel())
        }


    }


    /** Calls in raw **/

    private fun fiveBeers() {
        GlobalScope.async(Dispatchers.Default) {
            beerUseCase.fiveBeers().await().consumeEach {
                delay(1000)
                it?.let {
                    _beerCo.postValue(it.toBeerViewModel())
                }
            }
        }
    }




    private fun Beer.toBeerViewModel() = BeerView.Model(
        name = name,
        abv = abv ?: "??",
        image_url = image?.url
    )
}