package com.exmple.alexetnico.model

import java.io.Serializable

data class BreweryResult(
        val currentPage : Int,
        val numberOfPages : Int,
        val totalResults : Int,
        val data : List<Beer>?,
        val status :  String
) : Serializable