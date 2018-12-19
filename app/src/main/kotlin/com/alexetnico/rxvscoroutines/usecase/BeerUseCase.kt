package com.alexetnico.rxvscoroutines.usecase

import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.Executors

class BeerUseCase(val key: String) {
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }

    val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    var channel: Channel<Beer?> = Channel()


    /*********** Random ***********/

    suspend fun randomBeerCo(): Beer = GlobalScope.async(Dispatchers.Main) {
        coroutinesService.randomBeer(key).await().beer
    }.await()


    fun randomBeerRx(): Single<Beer> = rxService.randomBeer(key).map { it.beer }


    /*********** Beer with image ***********/

    suspend fun beerWithImageCo(): Beer =
        coroutinesService.beerImage(randomBeerCo().id, key).await().beer


    fun beerWithImageRx(): Single<Beer> = randomBeerRx()
        .flatMap { rxService.beerImage(it.id, key) }
        .map { it.beer }


    /*********** Calls in raw ***********/

    fun randomBeersCo(quantity: Int) = GlobalScope.async(coroutineContext) {
        channel = Channel(quantity)
        repeat(quantity) {
            channel.send(randomBeerCo())
        }
        channel.close()
    }

    fun randomBeersRx(quantity: Long): Observable<Beer> = randomBeerRx()
        .toObservable()
        .repeat(quantity)


    /***********  RECURSIVE  ***********/

    suspend fun beerWithSafeImageCo(): Beer {
        val beer = beerWithImageCo()
        return when (beer.image?.url.isNullOrBlank()) {
            true -> beerWithSafeImageCo()
            false -> beer
        }
    }

    fun beerWithSafeImageRx(): Single<Beer> = beerWithImageRx()
        .flatMap { beer ->
            if (beer.image?.url.isNullOrBlank()) beerWithSafeImageRx()
            else Single.just(beer)
        }

}