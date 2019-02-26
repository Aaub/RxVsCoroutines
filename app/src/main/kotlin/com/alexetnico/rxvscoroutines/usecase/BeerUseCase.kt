package com.alexetnico.rxvscoroutines.usecase

import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class BeerUseCase(val key: String) {
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }
    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }


    /*********** Random ***********/

    suspend fun randomBeerCo(): Beer = coroutinesService.randomBeer(key).await().beer


    fun randomBeerRx(): Single<Beer> = rxService.randomBeer(key).map { it.beer }


    /*********** Beer with image ***********/

    suspend fun beerWithImageCo(): Beer = coroutinesService.beerImage(randomBeerCo().id, key).await().beer


    fun beerWithImageRx(): Single<Beer> = randomBeerRx()
        .flatMap { rxService.beerImage(it.id, key) }
        .map { it.beer }


    /*********** Calls in raw ***********/

    suspend fun randomBeersCo(quantity: Int) = Channel<Beer?>(quantity).apply {
        CoroutineScope(Dispatchers.Default).launch {
            repeat(quantity) {
                send(randomBeerCo())
            }
            close()
        }
    }

    fun randomBeersRx(quantity: Long): Observable<Beer> = randomBeerRx()
        .toObservable()
        .repeat(quantity)


    /***********  RECURSIVE  ***********/

    suspend fun beerWithSafeImageCo(): Beer =
        beerWithImageCo().let {
            if (it.image?.url.isNullOrBlank()) beerWithSafeImageCo()
            else it
        }


    fun beerWithSafeImageRx(): Single<Beer> = beerWithImageRx()
        .flatMap { beer ->
            if (beer.image?.url.isNullOrBlank()) beerWithSafeImageRx()
            else Single.just(beer)
        }

}