package com.alexetnico.rxvscoroutines.repo.coroutines

import com.alexetnico.rxvscoroutines.model.BreweryResult
import com.alexetnico.rxvscoroutines.model.RandomResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface BreweryService {
    @GET("beers?")
    fun beers(@Query("key") key: String): Deferred<BreweryResult>

    @GET("beer/random?")
    fun randomBeer(@Query("key") key: String): Deferred<RandomResult>
}
