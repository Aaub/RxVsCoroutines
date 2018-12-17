package com.alexetnico.rxvscoroutines.usecase

import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
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

    /** Random **/

    suspend fun randomCo(): Deferred<Beer?> =

        GlobalScope.async(Dispatchers.Main) {
            coroutinesService.randomBeer(key).await().beer

        }



    fun randomRx(): Single<Beer> = rxService.randomBeer(key).map { it.beer }


    /** Beer with image **/

    fun beerWithImageCo(): Deferred<Beer?> =
        GlobalScope.async(coroutineContext) {
            randomCo().await()?.let {
                coroutinesService.beerImage(it.id, key).await().beer
            }
        }


    /** Calls in raw **/

    fun fiveBeers(): Deferred<Channel<Beer?>> =
        GlobalScope.async(coroutineContext) {
            val channel = Channel<Beer?>(5)
            for (i in 1..5) {
                channel.send(randomCo().await())
            }
            channel
        }





    fun beerWithImageRx(): Single<Beer> = randomRx()
        .flatMap { rxService.beerImage(it.id, key) }
        .map { it.beer }

    /** Calls in raw **/

    fun fiveBeers(): Deferred<Channel<Beer?>> =
        GlobalScope.async(coroutineContext) {
            val channel = Channel<Beer?>(5)
            for (i in 1..5) {
                channel.send(randomCo().await())
            }
            channel
        }






}