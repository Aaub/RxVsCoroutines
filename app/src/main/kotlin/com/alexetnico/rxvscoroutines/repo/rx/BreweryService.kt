package com.alexetnico.rxvscoroutines.repo.rx

import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.model.BreweryResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BreweryService {
    @GET("beers?")
    fun beers(@Query("key") key: String): Single<BreweryResult>


    @GET("beer/random?")
    fun randomBeer(@Query("key") key: String): Single<Beer> //TODO alex dit de la merde c'est pas un objet Beer
}