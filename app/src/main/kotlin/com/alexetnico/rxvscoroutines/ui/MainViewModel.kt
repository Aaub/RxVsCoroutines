package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.ui.customview.BeerView
import com.alexetnico.rxvscoroutines.usecase.BeerUseCase
import com.alexetnico.rxvscoroutines.utils.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val key: String) : ViewModel() {
    private val beerUseCase = BeerUseCase(key)

    private val _beerCo = MutableLiveData<BeerView.Model>()
    val beerCo: LiveData<BeerView.Model> = _beerCo

    private val _beerImageCo = MutableLiveData<String>()
    val beerImageCo: LiveData<String> = _beerImageCo

    private val _beerRx = MutableLiveData<BeerView.Model>()
    val beerRx: LiveData<BeerView.Model> = _beerRx


    fun fetchRandomBeer() {
        randomBeerRx()
        randomBeerCo()
    }

    /** Random **/

    private fun randomBeerCo() {
        _beerCo.postValue(beerUseCase.randomCo()?.toBeerViewModel())
    }

    private fun randomBeerRx() = beerUseCase
        .randomRx()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribeBy(
            onSuccess = { _beerRx.postValue(it.toBeerViewModel()) },
            onError = { Log.e("MainViewModel", it.message) }
        )


    /** Beer with image **/


//    private fun beerWithImage() {
//
//        val test2 = beerUseCase.randomCo().let {
//            it.copy(
//                image = GetBeerImage(it.id, key).beerImageUrl()
//            )
//        }
//        _beerCo.postValue(test2)
//
//    }


    private fun Beer.toBeerViewModel() = BeerView.Model(
        name = name,
        abv = abv,
        image_url = image?.url
    )

}