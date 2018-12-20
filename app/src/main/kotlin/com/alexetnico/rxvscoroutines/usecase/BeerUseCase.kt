package com.alexetnico.rxvscoroutines.usecase

import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class BeerUseCase(val key: String) {
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }


    var channel: Channel<Beer?> = Channel()


    /*********** Random ***********/

    suspend fun randomBeerCo(coroutineContext: CoroutineContext): Beer = GlobalScope.async(coroutineContext) {
        //        while (isActive){
//            delay(100)
//            Log.d("ESSAI : ", "isActive = $isActive")
//        }
        coroutinesService.randomBeer(key).await().beer
    }.await()


    fun randomBeerRx(): Single<Beer> = rxService.randomBeer(key).map { it.beer }


    /*********** Beer with image ***********/

    suspend fun beerWithImageCo(coroutineContext: CoroutineContext): Beer =
        withContext(coroutineContext) {
            coroutinesService.beerImage(randomBeerCo(coroutineContext).id, key).await().beer
        }


    fun beerWithImageRx(): Single<Beer> = randomBeerRx()
        .flatMap { rxService.beerImage(it.id, key) }
        .map { it.beer }


    /*********** Calls in raw ***********/

    fun randomBeersCo(
        quantity: Int,
        coroutineContext: CoroutineContext,
        coroutineScope: CoroutineScope
    ) = coroutineScope.async(coroutineContext) {
        channel = Channel(quantity)
        repeat(quantity) {
            channel.send(randomBeerCo(coroutineContext))
        }
        channel.close()
    }

    fun randomBeersRx(quantity: Long): Observable<Beer> = randomBeerRx()
        .toObservable()
        .repeat(quantity)


    /***********  RECURSIVE  ***********/

    suspend fun beerWithSafeImageCo(coroutineContext: CoroutineContext): Beer {
        return withContext(coroutineContext) {
            val beer = beerWithImageCo(coroutineContext)
            return@withContext when (beer.image?.url.isNullOrBlank()) {
                true -> beerWithSafeImageCo(coroutineContext)
                false -> beer
            }
        }
    }

    fun beerWithSafeImageRx(): Single<Beer> = beerWithImageRx()
        .flatMap { beer ->
            if (beer.image?.url.isNullOrBlank()) beerWithSafeImageRx()
            else Single.just(beer)
        }

}