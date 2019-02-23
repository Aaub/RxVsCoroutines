package com.alexetnico.rxvscoroutines.model

import com.google.gson.annotations.SerializedName

data class BeerResult(@SerializedName("data") val beer: Beer)