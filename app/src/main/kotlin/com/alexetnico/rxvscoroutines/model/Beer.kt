package com.alexetnico.rxvscoroutines.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Beer(
    val id: String,
    val name: String,
    val abv: String,
    val glasswareId: Int,
    @SerializedName("labels") val label: Label?
) : Serializable