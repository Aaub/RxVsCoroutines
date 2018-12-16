package com.alexetnico.rxvscoroutines.repo.rx

import com.alexetnico.rxvscoroutines.model.BreweryResult
import com.alexetnico.rxvscoroutines.model.RandomResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BreweryService {
    @GET("beersRx?")
    fun beers(@Query("key") key: String): Single<BreweryResult>

    @GET("beer/random?")
    fun randomBeer(@Query("key") key: String): Single<RandomResult>
}