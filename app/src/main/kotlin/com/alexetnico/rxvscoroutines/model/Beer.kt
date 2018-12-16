package com.alexetnico.rxvscoroutines.model

import java.io.Serializable

data class Beer(
    val id: String,
    val name: String,
    val abv: String,
    val glasswareId: Int,
    val labels: List<Label>?
) : Serializable