package com.alexetnico.rxvscoroutines.usecase

import android.util.Log
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import io.reactivex.Single
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class BeerUseCase(val key: String) {
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }

    val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    /** Random **/

    fun randomCo(): Beer? = runBlocking(coroutineContext, block = {
        try {
            async {
                coroutinesService.randomBeer(key).await()
            }.await().beer
        } catch (e: Exception) {
            Log.e("MainViewModel", e.message)
            null
        }

    })

    fun randomRx(): Single<Beer> = rxService.randomBeer(key).map { it.beer }


    /** Beer with image **/

//    fun beerWithImageCo(beerId : String): Beer? = runBlocking(coroutineContext, block = {
//        try {
//            async {
//                coroutinesService.beerImage(beerId, key).await().beer.image
//            }.await()
//        } catch (e: Exception) {
//            Log.e("MainViewModel", e.message)
//            null
//        }
//
//    })

    fun beerWithImageRx(): Single<Beer> = randomRx()
        .flatMap { rxService.beerImage(it.id, key) }
        .map { it.beer }

}