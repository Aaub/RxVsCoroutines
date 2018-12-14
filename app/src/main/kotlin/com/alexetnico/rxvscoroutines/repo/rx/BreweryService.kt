package com.alexetnico.rxvscoroutines.repo.rx

import com.alexetnico.rxvscoroutines.model.BreweryResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BreweryService {
    @GET("beers?")
    fun beers(@Query("key") key: String): Single<BreweryResult>
}