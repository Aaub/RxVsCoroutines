package com.alexetnico.rxvscoroutines.repo

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.alexetnico.rxvscoroutines.repo.coroutines.BreweryService as CoroutinesServices
import com.alexetnico.rxvscoroutines.repo.rx.BreweryService as RxServices


object BreweryApiServiceFactory {
    fun createRxService(): RxServices = buildRetrofit()

    fun createCoroutinesService(): CoroutinesServices = buildRetrofit()

    private inline fun <reified T> buildRetrofit() = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(T::class.java)

    private const val URL = "https://sandbox-api.brewerydb.com/v2/"
}