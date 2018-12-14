package com.alexetnico.rxvscoroutines.model

import java.io.Serializable

data class Glass(
    val id: Int,
    val name: String,
    val createdDate: String
) : Serializable