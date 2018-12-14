package com.alexetnico.rxvscoroutines.repo.coroutines

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BreweryApiServiceFactory {
    fun createService(): BreweryService = Retrofit.Builder()
            .baseUrl("https://sandbox-api.brewerydb.com/v2/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(BreweryService::class.java)
}