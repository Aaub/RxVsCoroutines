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

class GetBeer(val key: String) {
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }


    private val coroutineContext: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    fun randomBeerCo(): Beer? = runBlocking(coroutineContext, block = {
        try {
            async {
                coroutinesService.randomBeer(key).await()
            }.await().beer
        } catch (e: Exception) {
            Log.e("MainViewModel", e.message)
            null
        }

    })

    fun randomBeerRx(): Single<Beer> = rxService.randomBeer(key).map { it.beer }


}