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

class GetBeers(val key: String) {
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }


    private val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    fun beersCo(): List<Beer>? = runBlocking(coroutineContext, block = {
        try {
            async {
                coroutinesService.beers(key).await()
            }.await().beers
        } catch (e: Exception) {
            Log.e("MainViewModel", e.message)
            null
        }

    })


    fun beersRx(): Single<List<Beer>> = rxService.beers(key).map { it.beers }

}