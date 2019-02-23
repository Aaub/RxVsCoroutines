package com.alexetnico.rxvscoroutines.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BreweryResult(
    val currentPage: Int,
    val numberOfPages: Int,
    val totalResults: Int,
    @SerializedName("data") val beers: List<Beer>,
    val status: String
) : Serializable