package com.alexetnico.rxvscoroutines.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Image(
    @SerializedName("medium") val url: String
) : Serializable