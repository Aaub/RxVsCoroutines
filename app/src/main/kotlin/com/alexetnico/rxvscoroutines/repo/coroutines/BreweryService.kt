package com.alexetnico.rxvscoroutines.repo.coroutines

import com.alexetnico.rxvscoroutines.model.BeerResult
import com.alexetnico.rxvscoroutines.model.BreweryResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreweryService {
    @GET("beers?")
    fun beers(@Query("key") key: String): Deferred<BreweryResult>

    @GET("beer/random?")
    fun randomBeer(@Query("key") key: String): Deferred<BeerResult>

    @GET("beer/{beerId}?")
    fun beerImage(@Path("beerId") beerId: String, @Query("key") key: String): Deferred<BeerResult>
}
