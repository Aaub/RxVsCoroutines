package com.alexetnico.rxvscoroutines.usecase

import android.util.Log
import com.alexetnico.rxvscoroutines.model.Label
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class GetBeerImage(
    val key: String,
    val beerId: String
) : CustomCoroutineContext() {

    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    private val coroutinesService by lazy { BreweryApiServiceFactory.createCoroutinesService() }


    fun beerImageUrl(): Label? = runBlocking(coroutineContext, block = {
        try {
            async {
                coroutinesService.beerImage(beerId, key).await().beer.label
            }.await()
        } catch (e: Exception) {
            Log.e("MainViewModel", e.message)
            null
        }

    })

}