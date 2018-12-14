package com.alexetnico.rxvscoroutines.repo

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.alexetnico.rxvscoroutines.repo.coroutines.BreweryService as CoroutinesServices
import com.alexetnico.rxvscoroutines.repo.rx.BreweryService as RxServices


object BreweryApiServiceFactory {
    fun createRxService(): RxServices = Retrofit.Builder()
        .baseUrl("http://api.icndb.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RxServices::class.java)

    fun createCoroutinesService(): CoroutinesServices = Retrofit.Builder()
        .baseUrl("https://sandbox-api.brewerydb.com/v2/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(CoroutinesServices::class.java)
}