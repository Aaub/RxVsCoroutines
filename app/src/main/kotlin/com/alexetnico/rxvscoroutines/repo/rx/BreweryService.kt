package com.alexetnico.rxvscoroutines.repo.rx

import com.alexetnico.rxvscoroutines.model.BreweryResult
import io.reactivex.Single
import retrofit2.http.GET

interface BreweryService {
    @GET("toBeModified")
    fun beers(): Single<BreweryResult>
}