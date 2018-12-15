package com.alexetnico.rxvscoroutines.usecase

import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.repo.BreweryApiServiceFactory
import io.reactivex.Single

class BeerUseCase(val key: String) {
    private val rxService by lazy { BreweryApiServiceFactory.createRxService() }

    fun beers(): Single<List<Beer>> = rxService.beers(key).map { it.beers }

    fun randomBeer(): Single<Beer> = rxService.randomBeer(key).map { it.beer }
}